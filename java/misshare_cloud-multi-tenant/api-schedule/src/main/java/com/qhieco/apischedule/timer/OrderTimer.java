package com.qhieco.apischedule.timer;

import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.qhieco.apiservice.OrderService;
import com.qhieco.apiservice.PayService;
import com.qhieco.apiservice.impl.wx.WxSendTemplateService;
import com.qhieco.commonentity.*;
import com.qhieco.commonrepo.*;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.mapper.OrderMapper;
import com.qhieco.mapper.OrderRefundMapper;
import com.qhieco.push.QhMessageTemplate;
import com.qhieco.push.QhMessageType;
import com.qhieco.time.OrderMessageInfo;
import com.qhieco.time.OrderRefundInfo;
import com.qhieco.time.ReserveOrderInfo;
import com.qhieco.time.UnconfirmedOrderInfo;
import com.qhieco.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/27 11:47
 * <p>
 * 类说明：
 * 关于订单的定时器
 */
@Component
@Slf4j
public class OrderTimer {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderRefundMapper orderRefundMapper;

    @Autowired
    private OrderParkingRepository orderParkingRepository;

    @Autowired
    private ParklocRepository parklocRepository;

    @Autowired
    private ParklotRepository parklotRepository;

    @Autowired
    private PlateRepository plateRepository;

    @Autowired
    private PayService payService;

    @Autowired
    private UserExtraInfoRepository userExtraInfoRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private WxSendTemplateService wxSendTemplateService;

    @PersistenceContext
    EntityManager em;

    /**
     * 对未确认和已预约订单的超时处理
     */
    @Scheduled(cron = "0 0/1 * * * ?")
//    @Transactional(rollbackFor = Exception.class)
    public void scanOrder() {
        log.warn(" ------------------- 执行scanOrder 定时器  start -------------------- ");
        Long now = System.currentTimeMillis();
        // 1 未确认订单，超过免费预约时长后，取消其订单， 更新share和orderParking表的状态
        List<UnconfirmedOrderInfo> unconfirmedOrderInfoList = orderMapper.queryAllUnconfirmedOrderInfo(Status.OrderParking.UNCONFIRMED.getInt(),
                Constants.AUTO_CANCEL_RESERVED_TIME);
        log.info("存在未确认超时订单的个数，" + unconfirmedOrderInfoList.size());

        for (UnconfirmedOrderInfo unconfirmedOrderInfo : unconfirmedOrderInfoList) {
            try {
                orderService.updateUnconfirmedOrderTimeout(unconfirmedOrderInfo.getOrderId(), unconfirmedOrderInfo.getShareId());
            } catch (Exception e) {
                log.error(" 更新未确认超时订单信息异常， " + e);
            }
        }

        // 2 已预约订单，但未开始停车 且 超过保留车位的时长的订单，更新share和orderParking表的状态、物业和业主、平台的账户分成
        List<ReserveOrderInfo> reserveOrderInfoList = orderMapper.queryReserveOrderList(Status.OrderParking.RESERVED.getInt(),
                Status.Common.VALID.getInt(), Constants.MAX_DELAY_TIME);
        log.info("已预约订单，但未开始停车 且 超过保留车位的时长的订单的个数，" + reserveOrderInfoList.size());
        List<Message> messageList = new ArrayList<>();
        for (ReserveOrderInfo reserveOrderInfo : reserveOrderInfoList) {
            try {
                orderService.updateReservedOrderTimeout(reserveOrderInfo.getOrderId(), reserveOrderInfo.getShareId(),
                        reserveOrderInfo.getOwnerIncome(), reserveOrderInfo.getManageIncome());
                //发送极光推送
                OrderMessageInfo orderMessageInfo = orderMapper.queryOrderMessageInfo(reserveOrderInfo.getOrderId());
                Integer userId = orderMessageInfo.getUserId();
                Long startTime =orderMessageInfo.getStartTime();
                Long endTime = orderMessageInfo.getEndTime();
                String parklotName = orderMessageInfo.getParklotName();
                String parklocNumber = orderMessageInfo.getParklocNumber();
                String jpushId = orderMessageInfo.getJpushId();
                log.warn("------- 发送 距最晚入场时间15分钟提醒 userId is {}------",userId);
                String placeHolder = TimeUtil.timeStamp2Date(startTime, "MM月dd日HH:mm") + "-"
                        + TimeUtil.timeStamp2Date(endTime, "MM月dd日HH:mm") + parklotName + parklocNumber;
                if(!StringUtils.isEmpty(jpushId)){
                    QhPushUtil.getInstance().sendQhPush(jpushId, QhMessageType.RESERVE, QhMessageTemplate.CANCEL_PROMPT,
                            placeHolder);
                }

                String content = String.format(QhMessageTemplate.CANCEL_PROMPT, placeHolder);
                Message message = new Message(userId,Constants.MIN_NON_NEGATIVE_INTEGER,QhMessageType.RESERVE.getTitle(),content,"",Constants.MESSAGE_TYPE_PERSONAL,Constants.MESSAGE_KIND_JPUSH,Status.Common.VALID.getInt(),now);
                messageList.add(message);
            } catch (Exception e) {
                log.error(" 更新 已预约订单，但未开始停车 且 超过保留车位的时长的订单 异常， " + e);
            }
        }
        //批量插入消息
        this.batchInsert(messageList);
        log.warn(" ------------------- 执行scanOrder 定时器 end -------------------- ");
    }

    @Scheduled(cron = "0 0/3 * * * ?")
    public void refundTimer() {
        log.warn(" - - - - - -  退款查询定时器 start - ------------");
        List<OrderRefundInfo> orderRefundInfoList = orderRefundMapper.queryRefundOrderListByCondition(Status.Refund.PROCESSING.getInt());
        log.warn("退款总单数为 " + orderRefundInfoList.size());
        for (int i = 0, len = orderRefundInfoList.size(); i < len; i++) {

            OrderRefundInfo orderRefundInfo = orderRefundInfoList.get(i);
//            log.warn("第 " + (i + 1) + " 笔退款单信息=" + orderRefundInfo);
            // 支付宝渠道
            if (Constants.PAY_CHANNEL_ALIPAY == orderRefundInfo.getChannel()) {
                AlipayTradeFastpayRefundQueryResponse refundQueryResponse = payService.alipayRefundQuery(orderRefundInfo.getOrderTradeNo(), null, orderRefundInfo.getSerialNumber());
                String refundAmount = refundQueryResponse.getRefundAmount();
                if (StringUtils.isEmpty(refundAmount)) {
                    continue;
                }
                log.warn("支付宝支付退款响应参数，code=" + refundQueryResponse.getCode() + ", refundAmount = " + refundQueryResponse.getRefundAmount() + ", message=" + refundQueryResponse.getMsg());
                BigDecimal bRefundAmount = new BigDecimal(refundAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
                if ("10000".equals(refundQueryResponse.getCode())
                        && (bRefundAmount.compareTo(orderRefundInfo.getFee().setScale(2, BigDecimal.ROUND_HALF_UP)) == 0)) {
                    // 退款成功
                    orderService.updateOrderRefundAndOrderTotal(orderRefundInfo.getSerialNumber(), Status.Refund.PROCESS_SUCCESS_TOTAL.getInt());
                } else {
                    log.error("支付宝退款失败，" + refundQueryResponse.toString() + ", " + refundQueryResponse.getCode() + " , " + refundQueryResponse.getMsg() + ", 重新发起退款请求。");
                    payService.refund(orderRefundInfo.getOrderTradeNo(), orderRefundInfo.getChannel(), orderRefundInfo.getFee(), orderRefundInfo.getSerialNumber());
                }

                // 微信渠道
            } else if (Constants.PAY_CHANNEL_WXPAY == orderRefundInfo.getChannel() || Constants.PAY_CHANNEL_WXPAY_PUBLIC == orderRefundInfo.getChannel()
                    || Constants.PAY_CHANNEL_WXPAY_XCX == orderRefundInfo.getChannel()) {
                String response = payService.wxRefundQuery(orderRefundInfo.getOrderTradeNo(), null,
                        orderRefundInfo.getSerialNumber(), null, orderRefundInfo.getChannel());
                if (StringUtils.isEmpty(response)) {
                    continue;
                }

                Map<String, String> resMap = Xml.xmlString2Map(response);
                log.warn(" 微信支付退款查询接口响应参数 resMap= " + resMap);
                //SUCCESS退款申请接收成功，结果通过退款查询接口查询  || FAIL
                if (!resMap.containsKey("result_code") || !"SUCCESS".equals(resMap.get("result_code"))) {

                    if (resMap.containsKey("err_code") && "REFUNDNOTEXIST".equals(resMap.get("err_code"))) {
                        log.warn("订单不存在, 对此订单不作处理，订单信息=" + orderRefundInfo + ", 重新发起退款请求。");
                        payService.refund(orderRefundInfo.getOrderTradeNo(), orderRefundInfo.getChannel(), orderRefundInfo.getFee(), orderRefundInfo.getSerialNumber());
                        // 处理订单状态
//                        orderService.updateOrderRefundAndOrderTotal(orderRefundInfo.getSerialNumber(), Status.Refund.PROCESS_FAILED.getInt());
                    } else if (resMap.containsKey("err_code_des") && "订单已全额退款".equals("err_code_des")) {

                    }
                } else {
                    String sign = resMap.get("sign");
                    resMap.remove("sign");
                    if (!verify(resMap, sign)) {
                        // 校验不通过参数
                        log.error("校验不通过参数");
                        continue;
                    }
                    // 当前返回退款笔数
                    int refundCount = Integer.valueOf((String) resMap.get("refund_count"));
                    for (int j = 0; j < refundCount; j++) {
                        // 商户退款单号
                        String refundNo = (String) resMap.get("out_refund_no_" + j);
                        if (orderRefundInfo.getSerialNumber().equals(refundNo)) {
                            // SUCCESS—退款成功 REFUNDCLOSE—退款关闭 PROCESSING—退款处理中 CHANGE—退款异常
                            String refundState = (String) resMap.get("refund_status_" + j);

                            // 退款成功
                            if ("SUCCESS".equals(refundState)) {
                                orderService.updateOrderRefundAndOrderTotal(orderRefundInfo.getSerialNumber(), Status.Refund.PROCESS_SUCCESS_TOTAL.getInt());
                                // 失败的情况
                            } else if (!"PROCESSING".equals(refundState)) {
                                orderService.updateOrderRefundAndOrderTotal(orderRefundInfo.getSerialNumber(), Status.Refund.PROCESS_FAILED.getInt());
                            }
                            break;
                        }
                    }
                }
            }
        }
    }


    @Scheduled(cron = "0 0/5 * * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void sendMessage() {
        log.warn(" - - - - - -  发送消息定时器 start - ------------");
        long now = System.currentTimeMillis();
        long minutes15 = TimeUtil.minutesToMilliSeconds(15);
        long minutes5 = TimeUtil.minutesToMilliSeconds(5);
        long minutes10 = TimeUtil.minutesToMilliSeconds(10);
        // 两分半钟的浮动空间，参考定时器时长的一半
        long betweenTime = TimeUtil.secondToMilliSeconds(150);
        List<Integer> states = new ArrayList<>();
        states.add(Status.OrderParking.UNCONFIRMED.getInt());
        states.add(Status.OrderParking.RESERVED.getInt());
        states.add(Status.OrderParking.USED.getInt());
        states.add(Status.OrderParking.UNPAID.getInt());
        //查询所有未完成订单
        List<OrderParking> orderParkingList = orderParkingRepository.findByStates(states);
        if(null == orderParkingList || orderParkingList.size() == Constants.EMPTY_CAPACITY){
            return;
        }
        List<Integer> parklotIdList = new ArrayList();
        List<Integer> parklocIdList = new ArrayList();
        List<Integer> reservationIdList = new ArrayList<>();
        List<Integer> mobileUserIdList = new ArrayList<>();
        List<Integer> plateIdList = new ArrayList<>();
        for (OrderParking orderParking : orderParkingList){
            parklotIdList.add(orderParking.getParklotId());
            parklocIdList.add(orderParking.getParklocId());
            reservationIdList.add(orderParking.getReservationId());
            mobileUserIdList.add(orderParking.getMobileUserId());
            plateIdList.add(orderParking.getPlateId());
        }
        //查询所有和订单相关的停车场
        List<Parklot> parklotList = parklotRepository.findByIds(parklotIdList);
        //查询所有和订单相关的车位
        List<Parkloc> parklocList = parklocRepository.findByIds(parklocIdList);
        //查询所有和订单相关的预约数据
        List<Reservation> reservationList = reservationRepository.findByIds(reservationIdList);
        //查询所有和订单相关的车主账号数据
        List<UserExtraInfo> userExtraInfoList = userExtraInfoRepository.findByMobileUserIds(mobileUserIdList);
        //查询所有和订单相关的车牌号
        List<Plate> plateList = plateRepository.findByIds(plateIdList);
        Map<Integer, Parklot> parklotMap = new HashMap<>(128);
        Map<Integer, Parkloc> parklocMap = new HashMap<>(1024);
        Map<Integer, Reservation> reservationMap = new HashMap<>(1024);
        Map<Integer, UserExtraInfo> userExtraInfoMap = new HashMap<>(512);
        Map<Integer, Plate> plateMap = new HashMap<>(1024);
        //订单Id和车场的对应关系添加到parklotMap
        for(OrderParking orderParking : orderParkingList){
            for(Parklot parklot : parklotList){
                if(orderParking.getParklotId().equals(parklot.getId())){
                    parklotMap.put(orderParking.getId(),parklot);
                    break;
                }
            }
        }
        //订单Id和车位的对应关系添加到parklocMap
        for(OrderParking orderParking : orderParkingList){
            for(Parkloc parkloc : parklocList){
                if(orderParking.getParklocId().equals(parkloc.getId())){
                    parklocMap.put(orderParking.getId(),parkloc);
                    break;
                }
            }
        }
        //订单Id和预约的对应关系添加到reservationMap
        for(OrderParking orderParking : orderParkingList){
            for(Reservation reservation : reservationList){
                if(orderParking.getReservationId().equals(reservation.getId())){
                    reservationMap.put(orderParking.getId(),reservation);
                    break;
                }
            }
        }
        //用户Id和用户信息的对应关系添加到userExtraInfoMap
        for(OrderParking orderParking : orderParkingList){
            for(UserExtraInfo userExtraInfo : userExtraInfoList){
                if(orderParking.getMobileUserId().equals(userExtraInfo.getMobileUserId())){
                    userExtraInfoMap.put(orderParking.getMobileUserId(),userExtraInfo);
                    break;
                }
            }
        }
        //车牌Id和车牌的对应关系添加到plateMap
        for(OrderParking orderParking : orderParkingList){
            for(Plate plate : plateList){
                if(orderParking.getPlateId().equals(plate.getId())){
                    plateMap.put(orderParking.getPlateId(),plate);
                    break;
                }
            }
        }
        List<Message> messageList = new ArrayList<>();
        for(OrderParking orderParking : orderParkingList){
            try {
                Integer orderParkingId = orderParking.getId();
                Integer orderParkingState = orderParking.getState();
                Integer userId = orderParking.getMobileUserId();
                Integer plateId = orderParking.getPlateId();
                UserExtraInfo userExtraInfo = userExtraInfoMap.get(userId);
                Parkloc parkloc = parklocMap.get(orderParkingId);
                Parklot parklot = parklotMap.get(orderParkingId);
                if(null == parkloc || null == parklot || null == userExtraInfo){
                    log.warn("订单对应的停车场或停车位或车主第三方信息为空 orderParkingId is",orderParkingId);
                    continue;
                }
                Reservation reservation = reservationMap.get(orderParkingId);
                if(null == reservation){
                    log.warn("订单对应的预约记录为空 orderParkingId is",orderParkingId);
                    continue;
                }
                Plate plate = plateMap.get(plateId);
                if(null == plate){
                    log.warn("订单对应的车牌号为空 orderParkingId is",orderParkingId);
                    continue;
                }
                Long startTime = reservation.getStartTime();
                Long endTime = reservation.getEndTime();
                //入场提醒
                if(Status.OrderParking.RESERVED.getInt().equals(orderParkingState)){
                    // 还有十五分钟可以停车（中间有两分半钟的浮动区间）
                    long upTime = minutes15 + betweenTime;
                    long downTime = minutes15 - betweenTime;
                    boolean flag = (startTime - now <= upTime && startTime - now >= minutes15)
                            || (startTime - now >= downTime && startTime - now <= minutes15);
                    if(flag){
                        OrderParking parking = orderParkingRepository.findByReserveId(orderParkingId);
                        //跳过已经停车的订单
                        if(null != parking){
                            continue;
                        }
                        log.warn("------- 发送 距最晚入场时间15分钟提醒 userId is {}------",userId);
                        String placeHolder = "";
                        if(Constants.PARKING_APPOINTMENT == parklot.getType().byteValue()){
                            placeHolder = parklot.getName();
                        }
                        else if(Constants.PARKING_SHARE == parklot.getType().byteValue()){
                            placeHolder = parklot.getName() + parkloc.getNumber();
                        }

                        if(!StringUtils.isEmpty(userExtraInfo.getJpushRegId())){
                            QhPushUtil.getInstance().sendQhPush(userExtraInfo.getJpushRegId(), QhMessageType.RESERVE, QhMessageTemplate.ENTER_PROMPT,
                                    placeHolder);
                        }
                        String content = String.format(QhMessageTemplate.ENTER_PROMPT, placeHolder);
                        Message message = new Message(userId,Constants.MIN_NON_NEGATIVE_INTEGER,QhMessageType.RESERVE.getTitle(),content,"",Constants.MESSAGE_TYPE_PERSONAL,Constants.MESSAGE_KIND_JPUSH,Status.Common.VALID.getInt(),now);
                        messageList.add(message);
                        //如果用户微信OPEN ID 不为空发送微信公众号消息
                        String openId = userExtraInfo.getWxBindOpenId();
                        if(!StringUtils.isEmpty(openId)){
                            wxSendTemplateService.sendEnterTemplate(plate.getNumber(),placeHolder,TimeUtil.stampToDate(startTime),openId,orderParkingId);
                        }
                    }
                }
                else if(Status.OrderParking.USED.getInt().equals(orderParkingState)){
                    // 还有十五分钟离开提示（中间有两分半钟的浮动区间）
                    long upTime = minutes15 + betweenTime;
                    long downTime = minutes15 - betweenTime;
                    boolean flag = (endTime - now <= upTime && endTime - now >= minutes15)
                            || (endTime - now >= downTime && endTime - now <= minutes15);
                    if(flag){
                        log.warn("------- 发送 还有十五分钟离开提示 userId is {}------",userId);

                        String placeHolder = TimeUtil.timeStamp2Date(startTime, "MM月dd日HH:mm") + "-"
                                + TimeUtil.timeStamp2Date(endTime, "MM月dd日HH:mm");

                        if(Constants.PARKING_APPOINTMENT == parklot.getType().byteValue()){
                            placeHolder = placeHolder + parklot.getName();
                        }
                        else if(Constants.PARKING_SHARE == parklot.getType().byteValue()){
                            placeHolder = placeHolder + parklot.getName() + parkloc.getNumber();
                        }
                        if(!StringUtils.isEmpty(userExtraInfo.getJpushRegId())) {
                            QhPushUtil.getInstance().sendQhPush(userExtraInfo.getJpushRegId(), QhMessageType.RESERVE, QhMessageTemplate.LEAVE_PROMPT,
                                    placeHolder);
                        }
                        String content = String.format(QhMessageTemplate.LEAVE_PROMPT, placeHolder);
                        Message message = new Message(userId,Constants.MIN_NON_NEGATIVE_INTEGER,QhMessageType.RESERVE.getTitle(),content,"",Constants.MESSAGE_TYPE_PERSONAL,Constants.MESSAGE_KIND_JPUSH,Status.Common.VALID.getInt(),now);
                        messageList.add(message);
                        //如果用户微信OPEN ID 不为空发送微信公众号消息
                        String openId = userExtraInfo.getWxBindOpenId();
                        if(!StringUtils.isEmpty(openId)){
                            String parklotName = "";
                            if(Constants.PARKING_APPOINTMENT == parklot.getType().byteValue()){
                                parklotName = parklot.getName();
                            }
                            else if(Constants.PARKING_SHARE == parklot.getType().byteValue()){
                                parklotName = parklot.getName()+ parkloc.getNumber();
                            }
                            wxSendTemplateService.sendLeaveTemplate(plate.getNumber(),parklotName,TimeUtil.stampToDate(endTime),openId,orderParkingId);
                        }

                    }
                    //共享车位预约超时5分钟提醒
                    if(now > endTime && now - endTime >= minutes5 && now - endTime <= minutes10){
                        String placeHolder = TimeUtil.timeStamp2Date(startTime, "MM月dd日HH:mm") + "-"
                                + TimeUtil.timeStamp2Date(endTime, "MM月dd日HH:mm") + parklot.getName() + parkloc.getNumber();
                        if(!StringUtils.isEmpty(userExtraInfo.getJpushRegId())) {
                            QhPushUtil.getInstance().sendQhPush(userExtraInfo.getJpushRegId(), QhMessageType.RESERVE, QhMessageTemplate.TIMEOUT_PROMPT,
                                    placeHolder);
                        }
                        String content = String.format(QhMessageTemplate.TIMEOUT_PROMPT, placeHolder);
                        Message message = new Message(userId,Constants.MIN_NON_NEGATIVE_INTEGER,QhMessageType.RESERVE.getTitle(),content,"",Constants.MESSAGE_TYPE_PERSONAL,Constants.MESSAGE_KIND_JPUSH,Status.Common.VALID.getInt(),now);
                        messageList.add(message);
                    }
                }
            }
            catch (Exception e){
                log.error(" 发送消息异常， " + e);
            }
        }
        //批量插入消息列表
        this.batchInsert(messageList);
    }



    public boolean verify(Map<String, String> map, String sign) {
        String mapStr = MapUtil.join(map);
        String signNew = Signature.getSign(mapStr, Constants.WXPAY_KEY);
        boolean result = signNew.equals(sign);
        log.warn("微信数据校验结果：" + result);
        return result;
    }

    /**
     *  批量插入Publish
     * @param list
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchInsert(List<Message> list) {
        int size =  list.size();
        for (int i = 0; i < size; i++) {
            Message dd = list.get(i);
            em.persist(dd);
            // 每1000条数据执行一次，或者最后不足1000条时执行
            if (i % 1000 == 0 || i==(size-1)) {
                em.flush();
                em.clear();
            }
        }
    }



}

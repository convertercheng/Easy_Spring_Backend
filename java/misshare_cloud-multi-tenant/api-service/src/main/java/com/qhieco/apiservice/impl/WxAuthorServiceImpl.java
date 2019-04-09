package com.qhieco.apiservice.impl;

import com.qhieco.apiservice.ActivityService;
import com.qhieco.apiservice.ParklotAmountService;
import com.qhieco.apiservice.WxAuthorService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.apiservice.impl.barrier.BoshiBarrierService;
import com.qhieco.barrier.keytop.response.KeyTopParkingCostRespone;
import com.qhieco.commonentity.*;
import com.qhieco.commonentity.iotdevice.Lock;
import com.qhieco.commonentity.relational.UserPlateB;
import com.qhieco.commonrepo.*;
import com.qhieco.commonrepo.iot.LockRepository;
import com.qhieco.config.ConfigurationFiles;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.mapper.OrderMapper;
import com.qhieco.mapper.ParklotMapper;
import com.qhieco.mapper.StatisticsMapper;
import com.qhieco.push.QhMessageTemplate;
import com.qhieco.push.QhMessageType;
import com.qhieco.request.api.WxBindRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.api.*;
import com.qhieco.util.*;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/25 17:32
 * <p>
 * 类说明：
 * ${description}
 */
@Slf4j
@Service
public class WxAuthorServiceImpl implements WxAuthorService {

    @Autowired
    private UserExtraInfoRepository userExtraInfoRepository;

    @Autowired
    private OrderParkingRepository orderParkingRepository;

    @Autowired
    private UserMobileRepository userMobileRepository;

    @Autowired
    private BalanceUserRepository balanceUserRepository;

    @Autowired
    private PlateRepository plateRepository;
    @Autowired
    private UserPlateBRepository userPlateBRepository;

    @Autowired
    private ShareRepository shareRepository;

    @Autowired
    private ParklotMapper parklotMapper;

    @Autowired
    private ParklocRepository parklocRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private OrderTotalRepository orderTotalRepository;

    @Autowired
    private SmsRepository smsRepository;

    @Autowired
    ParklotAmountService parklotAmountService;

    @Autowired
    private ConfigurationFiles configuration;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private BoshiBarrierService boshiBarrierService;

    @Autowired
    private OrderParkingScanpayRepository orderParkingScanpayRepository;

    @Autowired
    private LogOperationMobileRepository logOperationMobileRepository;

    @Autowired
    private ParklotRepository parklotRepository;

    @Autowired
    private IntegralPermissionsLevelRepository integralPermissionsLevelRepository;

    @Autowired
    private LockRepository lockRepository;

    @Autowired
    private ParklotDistrictRepository parklotDistrictRepository;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private StatisticsMapper statisticsMapper;

    @Override
    public String wxAuthorLocation(String code, String parklotId, String ip) throws Exception {
        Map<String, String> wxMap = WxUtil.getOAuthOpenId(code, configuration.getWxMpAppId(), configuration.getWxMpSecret());
        log.info("wxMap=" + wxMap);
        OrderParking orderParking = null;
        StringBuilder stringBuilder = new StringBuilder(configuration.getMpUrlPrefix() + "ihomedt/#/");
        boolean flag = true;
        if (wxMap == null) {
            log.error(Status.ApiErr.NONEXISTENT_WX_MODEL.getMsg());
            return null;
        }
        String unionId = wxMap.get("unionId");
        String openId = wxMap.get("openId");
        UserExtraInfo userExtraInfo = userExtraInfoRepository.findByUserExtraInfo(unionId);
        /**
         * 新用户预约页面
         */
        if (userExtraInfo == null) {
            stringBuilder.append("reservationNew?unionId=" + unionId + "&openId=" + openId + "&parklotId=" + parklotId);
            return stringBuilder.toString();
        }
        List<Integer> list = new ArrayList<Integer>();
        list.add(Status.OrderParking.UNCONFIRMED.getInt());
        list.add(Status.OrderParking.RESERVED.getInt());
        list.add(Status.OrderParking.USED.getInt());
        list.add(Status.OrderParking.UNPAID.getInt());
        orderParking = orderParkingRepository.findAllByMobileUserIdParkOrder(userExtraInfo.getMobileUserId(), list);
        /**
         * 老用户还有未完成停车订单跳转预约页面
         */
        if (orderParking != null) {
            flag = false;
        }
        if (flag) {
            List<Integer> listId = orderParkingRepository.findAllByMobileUserIdState(userExtraInfo.getMobileUserId());
            if (listId != null && listId.size() == Constants.EMPTY_CAPACITY) {
                listId.add(-1);
            }
            orderParking = orderParkingRepository.findAllByMobileUserIdReserveOrder(userExtraInfo.getMobileUserId(), list, listId);
            if (orderParking != null) {
                flag = false;
            }
        }
        if (!flag) {
            log.info(" 老用户还有未完成停车订单 , order state = " + orderParking.getState());
            flag = false;
            /**
             * 已预约
             */
            if (Status.OrderParking.RESERVED.getInt().equals(orderParking.getState())) {
                stringBuilder.append("reservationInfo");
            }
            /**
             * 停车中
             */
            log.info("Status.OrderParking.UNPAID.getInt() {}", Status.OrderParking.UNPAID.getInt());
            log.info("Status.OrderParking.UNPAID.getInt().equals(orderParking.getState()) {}", Status.OrderParking.UNPAID.getInt().equals(orderParking.getState()));
            if (Status.OrderParking.USED.getInt().equals(orderParking.getState()) || Status.OrderParking.UNPAID.getInt().equals(orderParking.getState())) {
                stringBuilder.append("reservationPaking");
            }
            /**
             * 预约未支付
             */
            if (Status.OrderParking.UNCONFIRMED.getInt().equals(orderParking.getState())) {
                stringBuilder.append("reservationBookingUnpaid");
            }
            stringBuilder.append("?unionId=" + unionId + "&parklotId=" + parklotId + "&openId=" + openId + "&orderId=" + orderParking.getId() + "&userId=" + userExtraInfo.getMobileUserId());
        }
        /**
         * 老用户预约界面
         */
        if (flag) {
            stringBuilder.append("reservationOld?unionId=" + unionId + "&parklotId=" + parklotId + "&openId=" + openId + "&userId=" + userExtraInfo.getMobileUserId());
        }
        LogOperationMobile logOperationMobile = new LogOperationMobile();
        logOperationMobile.setMobileUserId(userExtraInfo.getMobileUserId());
        logOperationMobile.setOperateTime(System.currentTimeMillis());
        logOperationMobile.setSourceIp(ip);
        logOperationMobile.setType(Status.LogOperateType.TYPE_LOGIN.getInt());
        logOperationMobile.setSourceModel("微信扫码");
        logOperationMobile.setContent("用户登录");
        logOperationMobileRepository.save(logOperationMobile);
        log.info("flag +" + flag + ", 跳转页面地址为：" + stringBuilder.toString());
        return stringBuilder.toString();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Resp saveUserBind(WxBindRequest wxBindRequest) throws Exception {
        String phone = wxBindRequest.getPhone();
        WxBindRespData wxBindRespData = new WxBindRespData();
        OrderParking orderParking = null;
        String jpushId = wxBindRequest.getJpushId();
        boolean flag = true;
        if (!PhoneFormatCheckUtils.isPhoneLegal(phone)) {
            throw new QhieException(Status.ApiErr.PHONE_NUM_ERROR);
        }
        if (CommonUtil.isTimeStampInValid(wxBindRequest.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        String ip = wxBindRequest.getIp();
        String phoneModel = wxBindRequest.getPhoneModel();
        if (StringUtils.isEmpty(ip)) {
            throw new QhieException(Status.ApiErr.NONEXISTENT_USER_IP);
        }
        if (StringUtils.isEmpty(phoneModel)) {
            throw new QhieException(Status.ApiErr.NONEXISTENT_USER_MODEL);
        }
        Parklot parklot = parklotRepository.findOne(wxBindRequest.getParklotId());
        if (Constants.PARKING_APPOINTMENT == parklot.getInnershare()) {
            if (StringUtils.isEmpty(wxBindRequest.getPlateNumber())) {
                throw new QhieException(Status.ApiErr.NONEXISTENT_USER_PLATE);
            }
            if (!boshiBarrierService.validateParkingLotNumberInfo(wxBindRequest.getPlateNumber(), parklot.getExtraParklotId())) {
                throw new QhieException(Status.ApiErr.REPEAT_WHITE_LIST);
            }
        }
        UserMobile user = userMobileRepository.findByPhone(phone);
        UserLoginRepData.UserBean userBean;
        if (user == null) {
            log.info("初始化一个新用户");
            userBean = registerNewUser(phone);
            LogOperationMobile logOperationMobile = new LogOperationMobile();
            logOperationMobile.setMobileUserId(userBean.getId());
            logOperationMobile.setOperateTime(System.currentTimeMillis());
            logOperationMobile.setSourceIp(ip);
            logOperationMobile.setType(Status.LogOperateType.TYPE_LOGIN.getInt());
            logOperationMobile.setSourceModel("微信扫码");
            logOperationMobile.setContent("用户登录");
            logOperationMobileRepository.save(logOperationMobile);
        } else {
            log.info("用户已存在，查询用户的相关信息");
            userBean = loginUser(user, jpushId);
            if ("H5".equals(jpushId) || "wechat app".equals(jpushId)) {
                if (userBean.getType() == Status.userType.USERTYPE_TWO.getValue()) {
                    throw new QhieException(Status.ApiErr.ADMIN_NAME_TYPE);
                }
            }
        }
        wxBindRespData.setPhone(phone);
        wxBindRespData.setUserId(userBean.getId());
        wxBindRespData.setUnionId(wxBindRequest.getUnionId());
        wxBindRespData.setParklotId(wxBindRequest.getParklotId());
        UserExtraInfo userExtraInfos = null;
        UserExtraInfo userExtraInfo = null;
        userExtraInfos = userExtraInfoRepository.findUserExtraInfo(userBean.getId());
        if (userExtraInfos != null && userExtraInfos.getWxUnionId() != null) {
            throw new QhieException(Status.ApiErr.NONEXISTENT_WX_USER);
        }
        userExtraInfo = userExtraInfoRepository.findByUserExtraInfo(wxBindRequest.getUnionId());
        if (userExtraInfo != null) {
            throw new QhieException(Status.ApiErr.NONEXISTENT_WX_OPENID);
        }
        if (userExtraInfos == null) {
            userExtraInfos = new UserExtraInfo();
        }
        if (userBean.getType() == Status.userType.USERTYPE_TWO.getValue()) {
            throw new QhieException(Status.ApiErr.PLATE_NAME_TYPE);
        }
        Plate plate = plateRepository.findByNumberAndState(wxBindRequest.getPlateNumber(), Status.Common.VALID.getInt());
        userExtraInfos.setMobileUserId(userBean.getId());
        userExtraInfos.setWxUnionId(wxBindRequest.getUnionId());
        userExtraInfos.setWxBindOpenId(wxBindRequest.getOpenId());
        userExtraInfoRepository.save(userExtraInfos);
        if (plate != null) {
            List<UserPlateB> plateList = userPlateBRepository.findByMobileUserIdAndPlateIdAndState
                    (userExtraInfos.getMobileUserId(), plate.getId(), Status.Common.VALID.getInt());
            if (plateList.size() == Constants.EMPTY_CAPACITY) {
                UserPlateB userPlateB = new UserPlateB();
                userPlateB.setUpdateTime(System.currentTimeMillis());
                userPlateB.setMobileUserId(userExtraInfos.getMobileUserId());
                userPlateB.setPlateId(plate.getId());
                userPlateB.setState(Status.Common.VALID.getInt());
                userPlateBRepository.save(userPlateB);
            }
            log.info("进车牌来了：plateList.size()=" + plateList.size());
        }
        if (plate == null) {
            plate = new Plate();
            plate.setNumber(wxBindRequest.getPlateNumber());
            plate.setCreateTime(System.currentTimeMillis());
            plate.setState(Status.Common.VALID.getInt());
            plateRepository.save(plate);
            UserPlateB userPlateB = new UserPlateB();
            userPlateB.setUpdateTime(System.currentTimeMillis());
            userPlateB.setMobileUserId(userExtraInfos.getMobileUserId());
            userPlateB.setPlateId(plate.getId());
            userPlateB.setState(Status.Common.VALID.getInt());
            userPlateBRepository.save(userPlateB);
            log.info("新增车牌：plateId=" + plate.getId());

            /*
             * 绑定车牌活动统计数据
             */
            saveStaticeData(userPlateB);

        }
        List<Integer> list = new ArrayList<Integer>();
        list.add(Status.OrderParking.UNCONFIRMED.getInt());
        list.add(Status.OrderParking.RESERVED.getInt());
        list.add(Status.OrderParking.USED.getInt());
        list.add(Status.OrderParking.UNPAID.getInt());
        orderParking = orderParkingRepository.findAllByMobileUserIdParkOrder(userExtraInfos.getMobileUserId(), list);
        /**
         * 老用户还有未完成停车订单跳转预约页面
         */
        if (orderParking != null) {
            log.info("有未完成订单：orderParkingId=" + orderParking.getId());
            flag = false;
        }
        if (flag) {
            List<Integer> listId = orderParkingRepository.findAllByMobileUserIdState(userExtraInfos.getMobileUserId());
            if (listId != null && listId.size() == 0) {
                listId.add(-1);
            }
            orderParking = orderParkingRepository.findAllByMobileUserIdReserveOrder(userExtraInfos.getMobileUserId(), list, listId);
            if (orderParking != null) {
                flag = false;
            }
        }
        if (!flag && orderParking != null) {
            flag = false;
            wxBindRespData.setOrderState(orderParking.getState());
            wxBindRespData.setOrderId(orderParking.getId());
            wxBindRespData.setTotalFee(orderParking.getTotalFee());
            log.info("有未完成的订单，ID：" + orderParking.getId() + "状态：" + orderParking.getState() + " 价格:" + orderParking.getTotalFee());
        }
        /**
         * 没有未完成的订单
         */
        if (orderParking == null && flag) {
            log.info("没有未完成的订单");
            List<Integer> states = new ArrayList<Integer>();
            states.add(Status.OrderParking.UNCONFIRMED.getInt());
            states.add(Status.OrderParking.USED.getInt());
            states.add(Status.OrderParking.UNPAID.getInt());
            Integer unfinishedPlateOrderCount = orderParkingRepository.findOrderParkingByPlateIdAndStates(plate.getId(), states, Status.OrderParking.RESERVED.getInt());
            if (null != unfinishedPlateOrderCount && unfinishedPlateOrderCount > Constants.EMPTY_CAPACITY) {
                throw new QhieException(Status.ApiErr.EXIST_UNFINISHED_PLATE_ORDER);
            }
            Long now = System.currentTimeMillis();
            Long startTime = wxBindRequest.getStartTime();
            Long endTime = wxBindRequest.getEndTime();
            Long shareStartTime = wxBindRequest.getShareStartTime();
            Long shareEndTime = wxBindRequest.getShareEndTime();
            //验证时间是否合法
            if (endTime <= startTime || startTime <= now) {
                log.error("invaild time. parklotId:" + wxBindRequest.getParklotId());
                throw new QhieException(Status.ApiErr.RESERVE_TIME_ILLEGAL);
            }
            Integer parklocId = wxBindRequest.getParklocId();
            List<Share> shareList;
            //如果没有指定车位
            if(null == parklocId){
                //查询车场下该时间段可被预约的车位共享时段
                shareList = shareRepository.findByParklotIdAndStateAndStartTimeAndEndTime(wxBindRequest.getParklotId(), Status.Parkloc.PUBLISHED.getInt(), shareStartTime, shareEndTime);
                if (null == shareList || shareList.size() == Constants.EMPTY_CAPACITY) {
                    throw new QhieException(Status.ApiErr.REPEAT_SHARE_RESERVE);
                }
            }else{//如果指定车位
                //查询车位该时间段可被预约的共享时段
                shareList = shareRepository.findByParklocIdAndStateAndStartTimeAndEndTime(parklocId, Status.Parkloc.PUBLISHED.getInt(), shareStartTime, shareEndTime);
                if (null == shareList || shareList.size() == Constants.EMPTY_CAPACITY) {
                    throw new QhieException(Status.ApiErr.REPEAT_SHARE_RESERVE);
                }
            }

            Share share = shareList.get(Constants.FIRST_INDEX);
            //判断预约时间有没有在共享时间段内
            if (shareStartTime > startTime || shareEndTime < endTime) {
                throw new QhieException(Status.ApiErr.RESERVE_TIME_ILLEGAL);
            }
            //计算预约费用
            Long advanceTime = startTime - now;
            FeeRuleReserve feeRuleReserve = parklotMapper.parklotReserveFeeRule(wxBindRequest.getParklotId());
            if (null == feeRuleReserve) {
                throw new QhieException(Status.ApiErr.NONEXISTENT_FEE_RULE);
            }
            String[] finishTimes = feeRuleReserve.getFinishTime().split(Constants.DELIMITER_COMMA);
            String[] fees = feeRuleReserve.getFee().split(Constants.DELIMITER_COMMA);
            BigDecimal reserveFee = null;
            for (int i = 0; i < finishTimes.length; i++) {
                if (advanceTime <= TimeUtil.minutesToMilliSeconds(Integer.valueOf(finishTimes[i]))) {
                    reserveFee = new BigDecimal(fees[i]);
                    break;
                }
            }
            //如果不在时间段内取最高的收费
            if ((null == reserveFee) && finishTimes.length > Constants.MIN_NON_NEGATIVE_INTEGER) {
                reserveFee = new BigDecimal(fees[finishTimes.length - 1]);
            }
            if (null == reserveFee) {
                throw new QhieException(Status.ApiErr.NONEXISTENT_FEE_RULE);
            }
            Double integralPermissionsCoefficient = null;
            user = userMobileRepository.findOne(userBean.getId());
            if (null != user) {
                integralPermissionsCoefficient = integralPermissionsLevelRepository.queryReserveCoefficientByIntegral(user.getIntegral());

            }
            if (null == integralPermissionsCoefficient) {
                integralPermissionsCoefficient = 1d;
            }
            reserveFee = BigDecimal.valueOf(integralPermissionsCoefficient).multiply(reserveFee).setScale(2, BigDecimal.ROUND_UP);
            //减少并发问题，再次确认车位状态
            Parkloc parkloc = parklocRepository.findOne(share.getParklocId());
            //保存到预约表
            Reservation reservation = new Reservation(userExtraInfos.getMobileUserId(), share.getId(), startTime, endTime);
            Reservation resReservation = reservationRepository.save(reservation);
            if (null == resReservation || null == resReservation.getId()) {
                throw new QhieException(Status.ApiErr.INSERT_ERROR);
            }
            //生成订单号
            String serialNumber = OrderNoGenerator.getOrderNo(Constants.RESERVATION_ORDER, userExtraInfos.getMobileUserId().toString());
            //保存到订单表
            orderParking = new OrderParking(serialNumber, userExtraInfos.getMobileUserId(), resReservation.getId(), parkloc.getId(), wxBindRequest.getParklotId(), plate.getId(), reserveFee, now, Status.OrderParking.UNCONFIRMED.getInt());
            OrderParking resOrderParking = orderParkingRepository.save(orderParking);
            if (null == resOrderParking || null == resOrderParking.getId()) {
                throw new QhieException(Status.ApiErr.INSERT_ERROR);
            }
            //改变车位状态为已预约
            parklocRepository.updateState(parkloc.getId(), Status.Parkloc.RESERVED.getInt());
            //保存到订单总表
            OrderTotal orderTotal = new OrderTotal(userExtraInfos.getMobileUserId(), serialNumber, Constants.RESERVATION_ORDER, now, Constants.BIGDECIMAL_ZERO, Status.OrderParking.UNCONFIRMED.getInt());
            orderTotalRepository.save(orderTotal);
            wxBindRespData.setOrderId(resOrderParking.getId());
            wxBindRespData.setTotalFee(resOrderParking.getTotalFee());
            log.info("保存完成的订单：" + resOrderParking.getId() + "金额:" + resOrderParking.getTotalFee());
            //  更新该车场的parklot_amount表的数据
            parklotAmountService.updateParklotAmountInfoByParklotId(wxBindRequest.getParklotId(), "saveUserBind 地推绑定用户下单");

        }
        removeExcessState(phone);
        return RespUtil.successResp(wxBindRespData);
    }

    /**
     * 绑定车牌活动统计 表:t_statistics
     * @param
     */
    protected void saveStaticeData(UserPlateB userPlateB) {
        List<ActivityRespData> activityList = activityService.findActivityByType(Status.ActivityType.REGISTE.getInt());
        if (activityList == null || activityList.size()==0) {
            return;
        }
        Integer activityId = 0;
        for (ActivityRespData item: activityList) {
            StatisticsData.StatisticsBean data = new StatisticsData.StatisticsBean();
            data.setType(2);
            data.setUserId(userPlateB.getMobileUserId());
            data.setActivityId(item.getId());
            data.setValue(1);
            data.setCreateTime(System.currentTimeMillis());
            //参与统计
            statisticsMapper.saveStatisticsData(data);

            //触发统计
            data.setType(4);
            statisticsMapper.saveStatisticsData(data);
        }
    }

    /**
     * 首次下单活动统计 表:t_statistics
     * @param
     */
    protected void saveStaticeData(UserMobile userMobile) {
        if(userMobile == null){
            return;
        }else{
            if(userMobile.getIsIndexOrder()==1){
                return;
            }
        }
        List<ActivityRespData> activityList = activityService.findActivityByType(Status.ActivityType.FIRST_ORDER.getInt());
        if (activityList == null || activityList.size()==0) {
            return;
        }
        Integer activityId = 0;
        StatisticsData.StatisticsBean stati= null;
        for (ActivityRespData item: activityList) {
            StatisticsData.StatisticsBean data = new StatisticsData.StatisticsBean();
            data.setType(2);
            data.setUserId(userMobile.getId());
            data.setActivityId(item.getId());
            data.setValue(1);
            data.setCreateTime(System.currentTimeMillis());
            //参与统计
            statisticsMapper.saveStatisticsData(data);

            //触发统计
            data.setType(4);
            statisticsMapper.saveStatisticsData(data);
            if(item.getType().intValue()==Status.ActivityType.BINDING_PLATE.getInt()){
                activityId = item.getId().intValue();
                stati = new StatisticsData.StatisticsBean();
                stati=data;
            }
        }
        if (activityId == 0 || stati == null) {
            return;
        }

        // 2 绑车牌
        // 1 注册
        // 3 支付
        // 获取活动阶梯类型
        // 如果不存在阶梯类型就retuen，终止后面的操作。
        List<ActivityRespData> itemList = activityService.findActivityTriggerTypeById(activityId);
        Integer userId = userMobile.getId();
        for(int i = 1; i<itemList.size();i++){
            Integer type = itemList.get(i).getTriggerType();
            if(type.intValue()==1){
                // 2
                Integer sums = statisticsMapper.findActivityByTriggerType(userId, Status.ActivityType.REGISTE.getInt());
                if(sums==null||sums==0){
                    return;
                }
            }else if(type.intValue()==2){
                // 4
                Integer sums = statisticsMapper.findActivityByTriggerType(userId, Status.ActivityType.BINDING_PLATE.getInt());
                if(sums==null||sums==0){
                    return;
                }
            }else if(type.intValue()==3){
                // 3
                Integer sums = statisticsMapper.findActivityByTriggerType(userId, Status.ActivityType.FIRST_ORDER.getInt());
                if(sums==null||sums==0){
                    return;
                }
            }
        }

        //获奖统计
        stati.setType(3);
        statisticsMapper.saveStatisticsData(stati);
    }

    @Override
    public String wxAuthorOrderLocation(String openId) throws Exception {
        UserExtraInfo userExtraInfo = userExtraInfoRepository.findByWxBindOpenId(openId);
        boolean flag = true;
        StringBuilder stringBuilder = new StringBuilder(configuration.getMpUrlPrefix() + "ihomedt/#/");
        OrderParking orderParking = null;
        if (userExtraInfo == null) {
            return null;
        }
        String unionId = userExtraInfo.getWxUnionId();
        List<Integer> list = new ArrayList<Integer>();
        list.add(Status.OrderParking.UNCONFIRMED.getInt());
        list.add(Status.OrderParking.RESERVED.getInt());
        list.add(Status.OrderParking.USED.getInt());
        list.add(Status.OrderParking.UNPAID.getInt());
        orderParking = orderParkingRepository.findAllByMobileUserIdParkOrder(userExtraInfo.getMobileUserId(), list);
        /**
         * 老用户还有未完成停车订单跳转预约页面
         */
        if (orderParking != null) {
            flag = false;
        }
        if (flag) {
            List<Integer> listId = orderParkingRepository.findAllByMobileUserIdState(userExtraInfo.getMobileUserId());
            if (listId != null && listId.size() == Constants.EMPTY_CAPACITY) {
                listId.add(-1);
            }
            orderParking = orderParkingRepository.findAllByMobileUserIdReserveOrder(userExtraInfo.getMobileUserId(), list, listId);
            if (orderParking != null) {
                flag = false;
            }
        }
        if (!flag && orderParking != null) {
            log.info(" 老用户还有未完成停车订单 , order state = " + orderParking.getState());
            flag = false;
            /**
             * 已预约
             */
            if (Status.OrderParking.RESERVED.getInt().equals(orderParking.getState())) {
                stringBuilder.append("reservationInfo");
            }
            /**
             * 停车中
             */
            if (Status.OrderParking.USED.getInt().equals(orderParking.getState())) {
                stringBuilder.append("reservationPaking");
            }
            /**
             * 预约未支付
             */
            if (Status.OrderParking.UNCONFIRMED.getInt().equals(orderParking.getState())) {
                stringBuilder.append("reservationBookingUnpaid");
            }
            Integer parklotId = orderParking.getParklotId();
            stringBuilder.append("?unionId=" + unionId + "&parklotId=" + parklotId + "&openId=" + openId + "&orderId=" + orderParking.getId());
            return stringBuilder.toString();
        }
        return null;
    }

    @Override
    public String wxAuthorLocation(String code) throws Exception {
        Map<String, String> wxMap = WxUtil.getOAuthOpenId(code, configuration.getWxMpAppId(), configuration.getWxMpSecret());
        if (wxMap == null) {
            return null;
        }
        String wxUnionId = wxMap.get("unionId");
        String wxOpenId = wxMap.get("openId");
        UserExtraInfo userExtraInfo = userExtraInfoRepository.findByUserExtraInfo(wxUnionId);
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("?openId=" + wxOpenId + "&unionId=" + wxUnionId);
        String url = configuration.getMpUrlPrefix() + "ihomeh5/#/login";
        if (userExtraInfo != null && userExtraInfo.getMobileUserId() != null) {
            url = configuration.getMpUrlPrefix() + "ihomeh5/#/myAcount";
            UserMobile userMobile = userMobileRepository.findOne(userExtraInfo.getMobileUserId());
            stringBuffer.append("&id=" + userMobile.getId());
            stringBuffer.append("&type=" + userMobile.getType());
            if (userMobile.getType() == Status.userType.USERTYPE_TWO.getValue().byteValue()) {
                url = configuration.getMpUrlPrefix() + "ihomeh5/#/login";
            }
            return url + stringBuffer.toString();
        }
        return url + stringBuffer.toString();
    }

    /**
     * 注册一个新用户：
     *
     * @param phone 手机号码
     * @return 返回userBean
     */
    @Transactional(rollbackFor = SQLException.class)
    protected UserLoginRepData.UserBean registerNewUser(String phone) {
        UserLoginRepData.UserBean userBean = new UserLoginRepData.UserBean();
        /*
         * 初始化user表
         */
        UserMobile userMobile = saveUserMobile(phone);
        /*
         * 初始化三方信息表
         */
        //saveUserExtraInfo(userMobile, null);
        /*
         * 初始化用户金额表
         */
        saveBalanceUser(userMobile);
        /*
         * 设置UserBean的属性
         */
        userBean.setId(userMobile.getId());
        userBean.setType(userMobile.getType());
        userBean.setToken(Constants.TOKEN);

        /*
         * 首次下单活动统计数据
         */
        saveStaticeData(userMobile);

        return userBean;
    }

    /**
     * 将数据存储到表t_user_mobile中
     *
     * @param phone 手机号码
     */
    private UserMobile saveUserMobile(String phone) {
        UserMobile userMobile = new UserMobile();
        userMobile.setPhone(phone);
        userMobile.setRegisterTime(System.currentTimeMillis());
        userMobile.setLatestLoginTime(System.currentTimeMillis());
        userMobile.setType(Constants.PARKING_RENTEE);
        userMobile.setState(Status.Common.VALID.getInt());
        userMobile.setIntegral(Constants.USER_LEVEL);
        return userMobileRepository.save(userMobile);
    }

    /**
     * 存储数据到三方表t_user_extra_info中
     *
     * @param userMobile 存储的userMobile
     */
    private void saveUserExtraInfo(UserMobile userMobile, String jpushId) {
        UserExtraInfo userExtraInfo = new UserExtraInfo();
        userExtraInfo.setMobileUserId(userMobile.getId());
        userExtraInfo.setJpushRegId(jpushId);
        userExtraInfoRepository.save(userExtraInfo);
        //清空表中极光ID一样的数据
        userExtraInfoRepository.updateSameJpushRegId(userMobile.getId(), jpushId);
    }

    /**
     * 清除用户刷验证码的状态
     *
     * @param phone 手机号码
     */
    private void removeExcessState(String phone) {
        List<SMS> smsList = smsRepository.findByPhoneOrderByIdDesc(phone);
        if (null != smsList && smsList.size() > Constants.EMPTY_CAPACITY) {
            SMS sms = smsList.get(Constants.FIRST_INDEX);
            sms.setState(Status.Common.VALID.getInt());
            if (null == smsRepository.save(sms)) {
                throw new QhieException(Status.ApiErr.INSERT_ERROR);
            }
        }
    }

    /**
     * 存储数据到用户金额表中t_balance_user
     *
     * @param userMobile 存储的userMobile
     */
    private void saveBalanceUser(UserMobile userMobile) {
        BalanceUser balanceUser = new BalanceUser();
        balanceUser.setMobileUserId(userMobile.getId());
        balanceUser.setState(Status.Common.VALID.getInt());
        balanceUserRepository.save(balanceUser);
    }

    /**
     * 登录查询用户的信息
     *
     * @param user    用户
     * @param jpushId 极光推送id
     * @return 返回userBean
     */
    private UserLoginRepData.UserBean loginUser(UserMobile user, String jpushId) {
        UserLoginRepData.UserBean userBean = new UserLoginRepData.UserBean();
        Integer userId = user.getId();
        //更新三方信息表&&单点登录
        updateUserExtraInfo(userId, jpushId);
        updateLastestLoginTime(user);
        userBean.setId(userId);
        userBean.setType(user.getType());
        userBean.setToken(Constants.TOKEN);
        BalanceUser balanceUser = balanceUserRepository.findByMobileUserIdAndState(userId, Status.Common.VALID.getInt());
        if (balanceUser == null) {
            saveBalanceUser(user);
        }
        return userBean;
    }

    /**
     * 更新三方信息表
     *
     * @param userId  用户id
     * @param jpushId 极光推送id
     */
    private void updateUserExtraInfo(int userId, String jpushId) {
        UserExtraInfo userExtraInfo = userExtraInfoRepository.findByMobileUserId(userId);
        if (userExtraInfo != null) {
            String oldJpushId = userExtraInfo.getJpushRegId();
            if (!jpushId.equals(oldJpushId) && oldJpushId != null && !"".equals(oldJpushId)) {
                //如果本次传入极光Id和之前存的不一样，推送消息
                QhPushUtil.getInstance().sendQhPush(oldJpushId, QhMessageType.CUSTOM,
                        QhMessageTemplate.SIGIN_OUT, "");
                log.info("send {}: sigin out message, oldJpushId:{}", userId, oldJpushId);
            }
            //清空表中极光ID一样的数据
            userExtraInfoRepository.updateSameJpushRegId(userId, jpushId);
            userExtraInfo.setJpushRegId(jpushId);
            userExtraInfoRepository.save(userExtraInfo);
        }
    }

    /**
     * 更新最新的登录时间
     *
     * @param user UserMobile
     */
    private void updateLastestLoginTime(UserMobile user) {
        user.setLatestLoginTime(System.currentTimeMillis());
        if (null == userMobileRepository.save(user)) {
            throw new QhieException(Status.ApiErr.INSERT_ERROR);
        }
    }

    @Override
    public String wxUserOrderLocation(String orderId) throws Exception {
        StringBuilder stringBuilder = new StringBuilder(configuration.getMpUrlPrefix() + "ihomedt/#/");
        OrderParking orderParking = orderParkingRepository.findOne(Integer.parseInt(orderId));
        UserExtraInfo userExtraInfo = userExtraInfoRepository.findUserExtraInfo(orderParking.getMobileUserId());
        String unionId = userExtraInfo.getWxUnionId();
        String openId = userExtraInfo.getWxBindOpenId();
        if (orderParking.getReserveId() != null) {
            orderParking = orderParkingRepository.findOne(orderParking.getReserveId());
        }
        /**
         * 已预约
         */
        if (Status.OrderParking.RESERVED.getInt().equals(orderParking.getState())) {
            stringBuilder.append("reservationInfo");
        }
        /**
         * 停车中或离场未支付
         */
        if (Status.OrderParking.USED.getInt().equals(orderParking.getState()) || Status.OrderParking.UNPAID.getInt().equals(orderParking.getState())) {
            stringBuilder.append("reservationPaking");
        }
        /**
         * 预约未支付
         */
        if (Status.OrderParking.UNCONFIRMED.getInt().equals(orderParking.getState())) {
            stringBuilder.append("reservationBookingUnpaid");
        }
        log.info("orderParking.getState() {}", orderParking.getState());
        if (Status.OrderParking.PAID.getInt().equals(orderParking.getState()) ||
                Status.OrderParking.TIMEOUT.getInt().equals(orderParking.getState()) ||
                Status.OrderParking.USER_CANCELED.getInt().equals(orderParking.getState())) {
            stringBuilder.append("reservationComplete");
        }
        Integer parklotId = orderParking.getParklotId();
        stringBuilder.append("?unionId=" + unionId + "&parklotId=" + parklotId + "&openId=" + openId + "&orderId=" + orderParking.getId() + "&userId=" + orderParking.getMobileUserId());
        return stringBuilder.toString();
    }

    @Override
    public String scanpayAuthorRedirectUrl(String code, String parklotId) throws Exception {
        StringBuilder redirectUrl = new StringBuilder(configuration.getMpUrlPrefix() + "ihomefast/");

        Map<String, String> wxMap = WxUtil.getOAuthOpenId(code, configuration.getWxMpAppId(), configuration.getWxMpSecret());
        log.info("scanpayAuthorRedirectUrl wxMap=" + wxMap);
        if (wxMap == null) {
            log.error(Status.ApiErr.NONEXISTENT_WX_MODEL.getMsg());
            return null;
        }
        String unionId = wxMap.get("unionId");
        String openId = wxMap.get("openId");
        redirectUrl.append("?unionId=" + unionId + "&openId=" + openId + "&parklotId=" + parklotId);
        return redirectUrl.toString();
    }

    @Override
    public String queryParkingInfoRedirect(String plateNo, Integer parklotId, String unionId, String openId) throws Exception {
        StringBuilder builder = new StringBuilder();
        ScanpayOrderInfo orderInfo = orderMapper.queryReserveInfoByPlateNoAdParklotId(parklotId, plateNo);
        if (orderInfo == null) {
            orderInfo = orderMapper.queryParkingInfoByPlateNoAdParklotId(parklotId, plateNo);
        }
        log.info("查询此车牌是否通过预约进来， orderInfo = " + orderInfo);

        if (orderInfo == null) {
            builder.append("ihomefast/");

            ParkingInfoRespData parkingInfoRespData = this.queryParkingInfo(plateNo, parklotId, unionId, openId);
            // 车场名称
            builder.append("?parklotId=" + parklotId);
            builder.append("&unionId=" + unionId);
            builder.append("&plateNo=" + plateNo);
            builder.append("&openId=" + openId);
            builder.append("&result=" + parkingInfoRespData.getResult());
            builder.append("&payTime=" + parkingInfoRespData.getPayTime());
            builder.append("&fee=" + parkingInfoRespData.getFee());
            builder.append("&parklotName=" + (parkingInfoRespData.getParklotName()));
            if (StringUtils.isNotEmpty(parkingInfoRespData.getMessage())) {
                builder.append("&message=" + (parkingInfoRespData.getMessage()));
            }
            if (parkingInfoRespData.getEnterTime() != null) {
                builder.append("&enterTime=" + parkingInfoRespData.getEnterTime());
            }
            if (parkingInfoRespData.getStopTime() != null) {
                builder.append("&stopTime=" + parkingInfoRespData.getStopTime());
            }
            if (parkingInfoRespData.getFreeTime() != null) {
                builder.append("&freeTime=" + parkingInfoRespData.getFreeTime());
            }
        } else {
            this.reserved(orderInfo.getOrderId(), orderInfo.getOrderState(), unionId, orderInfo.getParklotId(),
                    openId, orderInfo.getMobileUserId(), builder);
        }
        return builder.toString();
    }

    public void reserved(Integer orderId, Integer orderState, String unionId, Integer parklotId, String openId,
                         Integer mobileUserId, StringBuilder stringBuilder) {
        stringBuilder.append("ihomedt/#/");
        //已预约
        if (Status.OrderParking.RESERVED.getInt().equals(orderState)) {
            stringBuilder.append("reservationInfo");
        }
        //停车中
        if (Status.OrderParking.USED.getInt().equals(orderState) || Status.OrderParking.UNPAID.getInt().equals(orderState)) {
            stringBuilder.append("reservationPaking");
        }
        // 预约未支付
        if (Status.OrderParking.UNCONFIRMED.getInt().equals(orderState)) {
            stringBuilder.append("reservationBookingUnpaid");
        }
        stringBuilder.append("?unionId=" + unionId + "&parklotId=" + parklotId + "&openId=" + openId + "&orderId=" + orderId + "&userId=" + mobileUserId + "&orderState=" + orderState);
    }

    @Override
    public ParkingInfoRespData queryParkingInfo(String plateNo, Integer parklotId, String unionId, String openId) {
        ParkingInfoRespData parkingInfoRespData = new ParkingInfoRespData();
        // 车场名称
        HashMap<String, String> parklotInfo = parklotMapper.queryParklotInfo(parklotId);
        parkingInfoRespData.setParklotName(parklotInfo.get("NAME"));
        parkingInfoRespData.setParklotId(parklotId);
        parkingInfoRespData.setUnionId(unionId);
        parkingInfoRespData.setPlateNo(plateNo);
        parkingInfoRespData.setOpenId(openId);

        KeyTopParkingCostRespone keyTopParkingCostRespone = boshiBarrierService.getPlateNumberByParkOrderCost(plateNo, System.currentTimeMillis());
        log.info("请求bsg接口车辆停车信息返回数据：" + keyTopParkingCostRespone);
        String result = keyTopParkingCostRespone.getData().getResult();
        parkingInfoRespData.setResult(result);
        parkingInfoRespData.setMessage(Status.BSGMessage.find(Integer.valueOf(result)));

        // 入场时间
        parkingInfoRespData.setEnterTime(DateUtils.dateStrConvertTimestamp(keyTopParkingCostRespone.getData().getEntranceDate(), "yyyy-MM-dd'T'HH:mm:ss"));
        // 停车时长
        Long stopTime = System.currentTimeMillis() - parkingInfoRespData.getEnterTime();
        parkingInfoRespData.setStopTime(stopTime < 0 ? 0 : stopTime);

        // result 为 2， 不需要缴费
        if (Status.BSGMessage.NO_NEED_PAY.getValue().toString().equals(result)) {
            parkingInfoRespData.setFee(BigDecimal.ZERO);
            // 离场倒计时
            String outTime = keyTopParkingCostRespone.getData().getOutTime();
            if (StringUtils.isNotEmpty(outTime)) {

                // 判断此车牌是否在outTime时间内已支付过订单
                long outTimeMilliSeconds = TimeUtil.minutesToMilliSeconds(Integer.valueOf(outTime));
                Long payTime = orderParkingScanpayRepository.queryPayTimeByPlateNo(plateNo, outTimeMilliSeconds, parkingInfoRespData.getEnterTime());
                if (payTime != null) {
                    // 支付时间
                    parkingInfoRespData.setPayTime(payTime);
                    // 免费时长倒计时
                    long freeTime = outTimeMilliSeconds - (System.currentTimeMillis() - payTime);
                    parkingInfoRespData.setFreeTime(freeTime < 0 ? 0 : freeTime);
                }
            }

        } else if (Status.BSGMessage.NORMAL.getValue().toString().equals(result)) {
            // 停车费用
            parkingInfoRespData.setFee(Money.ChinaFenToCNY(keyTopParkingCostRespone.getData().getPkorder().getOutstandingAmount()));
        }
        return parkingInfoRespData;
    }


    @Override
    public Resp getSmallRoutineAuthorInfo(WxBindRequest wxBindRequest){
        SmallRoutineData smallRoutineData = new SmallRoutineData();
        try {
            if (CommonUtil.isTimeStampInValid(wxBindRequest.getTimestamp())) {
                throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
            }
            byte[] resultByte  = AesUtil.decrypt(Base64.decodeBase64(wxBindRequest.getEncryptedData()), Base64.decodeBase64(wxBindRequest.getSessionKey()),
                    Base64.decodeBase64(wxBindRequest.getIv()));
            String userInfo="";
            if(null != resultByte && resultByte.length > 0){
                userInfo = new String(resultByte, "UTF-8");
            }else{
                throw new QhieException(Status.ApiErr.THIRD_PARTY_ERROR);
            }
            log.info("userInfo {}",userInfo);
            String unionId = JSONObject.fromObject(userInfo).getString("unionId");
            String openId = JSONObject.fromObject(userInfo).getString("openId");
            UserExtraInfo userExtraInfo = userExtraInfoRepository.findByUserExtraInfo(unionId);
            if (userExtraInfo != null) {
                smallRoutineData.setUserId(userExtraInfo.getMobileUserId());
                smallRoutineData.setUserPhone(userMobileRepository.findOne(userExtraInfo.getMobileUserId()).getPhone());

            }
            smallRoutineData.setUnionId(unionId);
            smallRoutineData.setOpenId(openId);
            if(StringUtils.isNotEmpty(wxBindRequest.getMacId())){
               Lock lock=lockRepository.findByBtName(wxBindRequest.getMacId());
                smallRoutineData.setParklocId(lock.getParklocId());
                Parkloc parkloc=parklocRepository.findOne(lock.getParklocId());
                smallRoutineData.setParkLotId(parkloc.getParklotId());
            }
            if(wxBindRequest.getParklotDistrictId()!=null){
             ParklotDistrict parklotDistrict=parklotDistrictRepository.findByIdAndState(wxBindRequest.getParklotDistrictId(),Status.Common.VALID.getInt());
             smallRoutineData.setParklotDistrictId(parklotDistrict.getId());
             smallRoutineData.setParkLotId(parklotDistrict.getParklotId());
            }
            List<OrderParking> orderParkings = null;
            if (smallRoutineData.getUserId() != null) {
                List<Integer> states = new ArrayList<Integer>();
                states.add(Status.OrderParking.UNCONFIRMED.getInt());
                states.add(Status.OrderParking.USED.getInt());
                states.add(Status.OrderParking.UNPAID.getInt());
                orderParkings = orderParkingRepository.findOrderParkingListByMobileUserIdAndStates(smallRoutineData.getUserId(), states, Status.OrderParking.RESERVED.getInt());
            }
            if (orderParkings != null && orderParkings.size() > 0) {
                OrderParkingLockData orderParkingLockData = new OrderParkingLockData();
                orderParkingLockData.setOrderState(orderParkings.get(Constants.FIRST_INDEX).getState());
                orderParkingLockData.setOrderId(orderParkings.get(Constants.FIRST_INDEX).getId());
                smallRoutineData.setOrderParkingLockData(orderParkingLockData);
            }
            if(wxBindRequest.getParklotId()!=null){
                smallRoutineData.setParkLotId(wxBindRequest.getParklotId());
            }
        } catch (Exception e) {
            log.error("解密异常 {}",e);
            throw new QhieException(Status.ApiErr.THIRD_PARTY_ERROR);
        }
        return RespUtil.successResp(smallRoutineData);
    }
}

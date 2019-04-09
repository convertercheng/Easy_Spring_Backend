package com.qhieco.apiservice.impl;

import com.google.gson.Gson;
import com.qhieco.apiservice.*;
import com.qhieco.apiservice.delaymessage.CycleQueue;
import com.qhieco.apiservice.delaymessage.Slot;
import com.qhieco.apiservice.delaymessage.Task;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.apiservice.impl.barrier.BoshiBarrierService;
import com.qhieco.apiservice.impl.redis.RedisService;
import com.qhieco.barrier.boostedgoal.request.BoostedGoalParkingRequest;
import com.qhieco.commonentity.*;
import com.qhieco.commonrepo.*;
import com.qhieco.commonrepo.iot.BalanceParklotRepository;
import com.qhieco.config.ConfigurationFiles;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.mapper.OrderMapper;
import com.qhieco.mapper.ParklotAmountMapper;
import com.qhieco.mapper.ParklotMapper;
import com.qhieco.mapper.StatisticsMapper;
import com.qhieco.request.api.ConfirmRequest;
import com.qhieco.request.api.OrderIdRequest;
import com.qhieco.request.api.OrderParkingRequest;
import com.qhieco.request.api.SaveSelectRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.api.OrderIdRepData;
import com.qhieco.response.data.api.OrderParkingLockData;
import com.qhieco.response.data.api.*;
import com.qhieco.util.*;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 11:38
 * <p>
 * 类说明：
 * 预约service实现类
 */
@Service
@Slf4j
public class ReserveServiceImpl implements ReserveService {

    @Autowired
    OrderParkingRepository orderParkingRepository;

    @Autowired
    OrderTotalRepository orderTotalRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    ParklotParamsBRepository parklotParamsBRepository;

    @Autowired
    ShareRepository shareRepository;

    @Autowired
    ParklocRepository parklocRepository;

    @Autowired
    FeeRuleReserveRepository feeRuleReserveRepository;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    PayService payService;

    @Autowired
    BalanceUserRepository balanceUserRepository;

    @Autowired
    ParklotAmountRepository parklotAmountRepository;

    @Autowired
    ParklotAmountMapper parklotAmountMapper;

    @Autowired
    private ParklotMapper parklotMapper;

    @Autowired
    OrderRefundRepository orderRefundRepository;

    @Autowired
    CouponOrderParkingBRepository couponOrderParkingBRepository;

    @Autowired
    private PublishService publishService;

    @Autowired
    private ParklotAmountService parklotAmountService;

    @Autowired
    private ReserveFilterRepository reserveFilterRepository;

    @Autowired
    private UserMobileRepository userMobileRepository;

    @Autowired
    private BalanceParklotRepository balanceParklotRepository;

    @Autowired
    ParklotRepository parklotRepository;

    @Autowired
    PlateRepository plateRepository;

    @Autowired
    private ConfigurationFiles configuration;

    @Autowired
    private BarrierApiService barrierApiService;

    @Autowired
    private IntegralPermissionsLevelRepository integralPermissionsLevelRepository;

    @Autowired
    private BoshiBarrierService boshiBarrierService;

    @Resource
    private PlatformTransactionManager transactionManager;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private LockService lockService;


    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RedisService redisService;

    @Autowired
    private StatisticsMapper statisticsMapper;

    @Autowired
    private ActivityService activityService;

    @Override
    public Resp confirm(ConfirmRequest confirmRequest) {

        //对停车场id加锁，避免多线程操作并发操作同一停车场订单
        RLock parklocLock = redissonClient.getLock("parkloc_lock_" + confirmRequest.getParklot_id().toString());
        //对用户id的加锁，避免同一用户并发定到多个订单
        RLock userLock = redissonClient.getLock("user_loc_" + confirmRequest.getUser_id().toString());
        //内存中记录该用户选取的订单
        RBucket<String> userOrder = redissonClient.getBucket("user_order_"+confirmRequest.getUser_id());
        //验证时间戳是否合法
        if (CommonUtil.isTimeStampInValid(confirmRequest.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        if (confirmRequest.getPlate_id() <= Constants.EMPTY_CAPACITY) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        if (confirmRequest.getStart_time() == null && confirmRequest.getIsImmediatelyEnter() == null) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        Long now = System.currentTimeMillis();
        Integer userId = confirmRequest.getUser_id();
        Integer parklotId = confirmRequest.getParklot_id();
        Integer plateId = confirmRequest.getPlate_id();
        Long  startTime = confirmRequest.getStart_time();
        if (confirmRequest.getIsImmediatelyEnter() != null && confirmRequest.getIsImmediatelyEnter() == 1) {
            startTime = now;
        }
        Long endTime = confirmRequest.getEnd_time();
        Parklot parklot = parklotRepository.findOne(parklotId);
        if (null == parklot) {
            throw new QhieException(Status.ApiErr.NONEXISTENT_PARKLOT);
        }
        //如果车场是内部共享判断道闸白名单
        if(Constants.PARKING_APPOINTMENT == parklot.getInnershare()){
            Plate plate = plateRepository.findOne(plateId);
            if(null == plate){
                throw new QhieException(Status.ApiErr.NONEXISTENT_USER_PLATE);
            }
            if(!boshiBarrierService.validateParkingLotNumberInfo(plate.getNumber(),parklot.getExtraParklotId())){
                throw new QhieException(Status.ApiErr.REPEAT_WHITE_LIST);
            }
        }

        //验证时间是否合法
        if (endTime <= startTime || startTime < now) {
            log.error("invaild time. parklotId:" + parklotId + ", confirmRequest = " + confirmRequest);
            throw new QhieException(Status.ApiErr.RESERVE_TIME_ILLEGAL);
        }
        Long shareStartTime = confirmRequest.getShare_startTime();
        Long shareEndTime = confirmRequest.getShare_endTime();

        //判断预约时间有没有在共享时间段内
//        if(shareStartTime > startTime || shareEndTime < endTime) {
        if (shareEndTime < endTime) {
            log.info("confirmRequest =" + confirmRequest);
            throw new QhieException(Status.ApiErr.RESERVE_TIME_ILLEGAL);
        }
        UserMobile userMobile;
        //读取用户状态，对用户id加读锁
        userMobile = userMobileRepository.findOne(userId);
//        log.info("Status.userType.USERTYPE_TWO.equals(userMobile.getType())",Status.userType.USERTYPE_TWO.getValue().byteValue()==userMobile.getType());
        if (Status.userType.USERTYPE_TWO.getValue().byteValue() == userMobile.getType()) {
            throw new QhieException(Status.ApiErr.PLATE_NAME_TYPE);
        }
        List<Integer> states = new ArrayList<>();
        states.add(Status.OrderParking.UNCONFIRMED.getInt());
        states.add(Status.OrderParking.USED.getInt());
        states.add(Status.OrderParking.UNPAID.getInt());
        //查询车牌号是否有未完成的订单
        Integer unfinishedPlateOrderCount = orderParkingRepository.findOrderParkingByPlateIdAndStates(plateId, states, Status.OrderParking.RESERVED.getInt());
        if (null != unfinishedPlateOrderCount && unfinishedPlateOrderCount > Constants.EMPTY_CAPACITY) {
            throw new QhieException(Status.ApiErr.EXIST_UNFINISHED_PLATE_ORDER);
        }
        //查询用户是否有未完成的订单
        Integer unfinishedOrderCount = orderParkingRepository.findOrderParkingByMobileUserIdAndStates(userId, states, Status.OrderParking.RESERVED.getInt());
        if (null != unfinishedOrderCount && unfinishedOrderCount > Constants.EMPTY_CAPACITY) {
            throw new QhieException(Status.ApiErr.EXIST_UNFINISHED_ORDER);
        }
        else {
            //锁定用户，避免用户定多个单
            userLock.lock(30, TimeUnit.SECONDS);
            if( userOrder.get()!=null){
                //如果此刻其他线程的同一用户完成了一个订单，那么释放锁，抛出异常
                userLock.unlock();
                throw new QhieException(Status.ApiErr.EXIST_UNFINISHED_ORDER);
            }
        }

        Parkloc parkloc;
        Share share = null;
        //开启事务,由于在内存中加了锁，所以事务级别可以采用最低级的隔离级别
        DefaultTransactionDefinition parklocDef = new DefaultTransactionDefinition();
        TransactionStatus parklocStatus = transactionManager.getTransaction(parklocDef);
        //检查与更新车位信息，加锁
        parklocLock.lock(30, TimeUnit.SECONDS);
        try {
            try {
                Integer parklocId = confirmRequest.getParkloc_id();
                Integer districtId = confirmRequest.getDistrict_id();
                List<Share> shareList;
                if(null != parklocId){
                    //查询车位下该时间段可被预约的车位共享时段
                    shareList = shareRepository.findByParklocIdAndStateAndStartTimeAndEndTime(parklocId, Status.Parkloc.PUBLISHED.getInt(), shareStartTime, shareEndTime);
                    if (null == shareList || shareList.size() == Constants.EMPTY_CAPACITY) {
                        throw new QhieException(Status.ApiErr.REPEAT_SHARE_RESERVE);
                    }
                }else if(null != districtId){
                    log.info("区域ID:=====districtId===={}",districtId);
                    //查询区域下该时间段可被预约的车位共享时段
                    shareList = shareRepository.findByDistrictIdAndStateAndStartTimeAndEndTime(districtId, Status.Parkloc.PUBLISHED.getInt(), shareStartTime, shareEndTime);
                    if (null == shareList || shareList.size() == Constants.EMPTY_CAPACITY) {
                        throw new QhieException(Status.ApiErr.REPEAT_SHARE_RESERVE);
                    }
                }else{
                    //确认车位状态//查询车场下该时间段可被预约的车位共享时段
                    shareList = shareRepository.findByParklotIdAndStateAndStartTimeAndEndTime(parklotId, Status.Parkloc.PUBLISHED.getInt(), shareStartTime, shareEndTime);
                    if (null == shareList || shareList.size() == Constants.EMPTY_CAPACITY) {
                        throw new QhieException(Status.ApiErr.REPEAT_SHARE_RESERVE);
                    }
                }

                // 判断车锁是否可以正常使用
                log.info("shareList.size  = " + shareList.size());
//                share = shareList.get(0);
                for (int i = 0; i < shareList.size(); i++) {
                    if (lockService.checkLockAvailable(shareList.get(i).getParklocId())) {
                        share = shareList.get(i);
                        break;
                    }
                }
                if (share == null) {
                    log.error("该时间段没有可预约的空车位，" + confirmRequest);
                    throw new QhieException(Status.ApiErr.EMPTY_PARKLOC);
                }

                parkloc = parklocRepository.findOne(share.getParklocId());
                //改变车位状态为已预约
                parklocRepository.updateState(parkloc.getId(), Status.Parkloc.RESERVED.getInt());
                //在内存中记录，用户即将生成预约订单
                userOrder.set(userMobile.getId().toString(), 3, TimeUnit.SECONDS);
            } finally {
                //确保锁一定被释放
                parklocLock.unlock();
                userLock.unlock();
            }
            //计算预约费用
            BigDecimal reserveFee = null;

            if (confirmRequest.getIsImmediatelyEnter() == null || confirmRequest.getIsImmediatelyEnter() != 1) {
                Long advanceTime = startTime - now;
                FeeRuleReserve feeRuleReserve = parklotMapper.parklotReserveFeeRule(parklotId);
                if (null == feeRuleReserve) {
                    throw new QhieException(Status.ApiErr.NONEXISTENT_FEE_RULE);
                }
                String[] finishTimes = feeRuleReserve.getFinishTime().split(Constants.DELIMITER_COMMA);
                String[] fees = feeRuleReserve.getFee().split(Constants.DELIMITER_COMMA);

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
                if (null != userMobile) {
                    integralPermissionsCoefficient = integralPermissionsLevelRepository.queryReserveCoefficientByIntegral(userMobile.getIntegral());

                }
                if (null == integralPermissionsCoefficient) {
                    integralPermissionsCoefficient = 1d;
                }
                reserveFee = BigDecimal.valueOf(integralPermissionsCoefficient).multiply(reserveFee).setScale(2, BigDecimal.ROUND_UP);

            } else if (confirmRequest.getIsImmediatelyEnter() == 1) {
                log.info("立即预约，预约费用为0");
                reserveFee = BigDecimal.ZERO;
            }
            //保存到预约表
            Reservation reservation = new Reservation(userId, share.getId(), startTime, endTime);
            Reservation resReservation = reservationRepository.save(reservation);
            if (null == resReservation || null == resReservation.getId()) {
                throw new QhieException(Status.ApiErr.INSERT_ERROR);
            }
            //生成订单号
            String serialNumber = OrderNoGenerator.getOrderNo(Constants.RESERVATION_ORDER, userId.toString());
            //保存到订单表
            OrderParking orderParking = new OrderParking(serialNumber, userId, resReservation.getId(), parkloc.getId(), parklotId, plateId, reserveFee, now, Status.OrderParking.UNCONFIRMED.getInt());
            OrderParking resOrderParking = orderParkingRepository.save(orderParking);
            if (null == resOrderParking || null == resOrderParking.getId()) {
                throw new QhieException(Status.ApiErr.INSERT_ERROR);
            }
            //保存到订单总表
            OrderTotal orderTotal = new OrderTotal(userId, serialNumber, Constants.RESERVATION_ORDER, now, Constants.BIGDECIMAL_ZERO, Status.OrderParking.UNCONFIRMED.getInt());
            orderTotalRepository.save(orderTotal);
            //回传订单Id，和费用
            OrderIdRepData data = new OrderIdRepData();
            data.setOrderId(resOrderParking.getId());
            data.setTotalFee(resOrderParking.getTotalFee());
            //  更新该车场的parklot_amount表的数据
            parklotAmountService.updateParklotAmountInfoByParklotId(parklotId, "confirm 预约车位");
            addToCycleQueue(orderParking);
            /*
             * 首次下单活动统计数据
             */
            saveStaticeData(userMobile);

            transactionManager.commit(parklocStatus);
            return RespUtil.successResp(data);
        }catch (Exception e){
            transactionManager.rollback(parklocStatus);
            //发生异常，应该在回滚之后将内存中记录的用户下单状态清除
            userOrder.delete();
            throw e;
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

    /**
     * 将该订单取消任务加入环形队列中
     *
     * @param orderParking 订单
     */
    private void addToCycleQueue(OrderParking orderParking) {
        // delay是秒数
        Long delay = 5 * 60L;
        Integer cycleNum = (int) (delay / CycleQueue.TOTAL_LEN);
        Task task = new Task("超时取消订单{id=" + orderParking.getId() + "}", cycleNum, () -> {
            // 检测该订单状态，如果还是未支付，那么改变该订单状态
            OrderParking orderParking1 = orderParkingRepository.findOne(orderParking.getId());
            if (orderParking1.getState().equals(Status.OrderParking.UNCONFIRMED.getInt())) {
                orderParking1.setState(Status.OrderParking.TIMEOUT.getInt());
                if (null == orderParkingRepository.save(orderParking1)) {
                    throw new QhieException(Status.ApiErr.INSERT_ERROR);
                }
                // 更新车位状态为已发布
                parklotMapper.updateParklocStateByOrderId(orderParking.getId(), Status.Parkloc.PUBLISHED.getInt());
                //更新order_total表的状态为超时
                orderMapper.updateOrderTotalStateByOrderId(orderParking.getId(), Status.OrderParking.TIMEOUT.getInt());
                publishService.dealWithToBePublish(orderParking);
            }
        });
        CycleQueue cycleQueue = CycleQueue.getInstance();
        Integer currentSlot = cycleQueue.getCurrentSlot();
        int destIndex = currentSlot + (int) (delay % CycleQueue.TOTAL_LEN);
        Slot[] queue = cycleQueue.getCycleQueue();
        if (queue[destIndex] == null) {
            queue[destIndex] = new Slot();
        }
        queue[destIndex].getTaskSet().add(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp cancel(OrderIdRequest orderIdRequest) {
        //验证时间戳是否合法
        if (CommonUtil.isTimeStampInValid(orderIdRequest.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        //查询该订单的状态是否可以取消
        Integer orderId = orderIdRequest.getOrder_id();
        Long now = System.currentTimeMillis();
        OrderParking orderParking = orderParkingRepository.findOne(orderId);
        if (null == orderParking) {
            throw new QhieException(Status.ApiErr.NONEXISTENT_ORDER_PARKING);
        }
        Integer orderState = orderParking.getState();
        Integer userId = orderParking.getMobileUserId();
        Boolean isCanceled = !(Status.OrderParking.UNCONFIRMED.getInt().equals(orderState) || Status.OrderParking.RESERVED.getInt().equals(orderState));
        if (isCanceled) {
            throw new QhieException(Status.ApiErr.REPEAT_ORDER_CANCEL);
        }
        //查询该订单是否已经停车
        OrderParking orderParking1 = orderParkingRepository.findByReserveId(orderId);
        if (null != orderParking1) {
            throw new QhieException(Status.ApiErr.ORDER_ALREADY_PARKING);
        }
        //查询redis中该订单是否有缓存的不可取消时间
        String redisValue = redisService.getStr(orderId.toString());
        if(!StringUtils.isEmpty(redisValue)){
            throw new QhieException(Status.ApiErr.PARKLOC_CANT_CANCEL);
        }
        Integer parklocId = orderParking.getParklocId();
        //免费取消预约的提前时间
        String value = parklotParamsBRepository.findValueByParklocId(parklocId, Constants.FREE_CANCELLATION_TIME, Status.Common.VALID.getInt());
        log.info("免费取消预约的时间：" + value);
        if (StringUtils.isEmpty(value)) {
            value = Constants.FREE_CANCELLATION_TIME_DEFAULT;
        }

        Long freeCancellationTime = TimeUtil.minutesToMilliSeconds(Integer.valueOf(value));
        //取消订单
        orderParkingRepository.updateStateAndCancelTime(orderId, Status.OrderParking.USER_CANCELED.getInt(), now, null);
        //车位置为已发布状态
        parklocRepository.updateState(parklocId, Status.Parkloc.PUBLISHED.getInt());
        try {
            //获取用户id的订单缓存，进行释放
            RBucket<String> userOrder = redissonClient.getBucket("user_order_"+orderParking.getMobileUserId());
            userOrder.delete();
        }catch (Exception e){
            log.error(e.getMessage());
        }
        //更新订单总表状态
        orderTotalRepository.updateStateBySerialNumber(orderParking.getSerialNumber(), Status.OrderParking.USER_CANCELED.getInt());
        Long payTime = orderParking.getPayTime();
        BigDecimal realFee = orderParking.getRealFee();
        //判断是否需要返还优惠券，免费取消预约时间之前才能返还优惠券
        Coupon coupon = couponRepository.findCouponByOrderId(orderId);
        if (null != coupon) {
            if (null != payTime && now - payTime < freeCancellationTime) {
                Integer couponId = coupon.getId();
                //删除关联关系
                couponOrderParkingBRepository.cancel(orderId, couponId, Status.Common.DELETED.getInt(), now);
                //改变支付渠道
                orderParkingRepository.updatePayChannel(orderId, Constants.PAY_CHANNEL_NONE);
                //如果优惠券已经使用了，改变优惠券状态
                if (coupon.getState().equals(Status.Coupon.USED.getInt())) {
                    //优惠券置为有效状态
                    couponRepository.updateStateAndUseTimeAndUseMoneyById(couponId, Status.Coupon.COUPON_CONVERTIBILITY.getInt(), null, Constants.BIGDECIMAL_ZERO);
                    log.info("返还优惠券couponId:" + couponId);
                    //更新分成并修改支付方式
                    orderParkingRepository.updateIncomeAndPayChannel(orderId, Constants.BIGDECIMAL_ZERO, Constants.BIGDECIMAL_ZERO, Constants.BIGDECIMAL_ZERO, Constants.PAY_CHANNEL_NONE);
                }
            }
        }
        //判断是否需要退款
        BigDecimal refundFee = Constants.BIGDECIMAL_ZERO;
        if (null != payTime && now - payTime < freeCancellationTime && null != realFee && (Constants.BIGDECIMAL_ZERO.compareTo(realFee) != 0)) {
            refundFee = realFee;
            log.info("开始退款流程orderParkingId:" + orderParking.getId());
            //生成退款订单号
            String serialNumber = OrderNoGenerator.getOrderNo(Constants.REFUND_ORDER, userId.toString());
            //开始退款流程
            Integer channel = orderParking.getPayChannel();
            log.info("channel{}   channel",channel);
            if (Constants.PAY_CHANNEL_ALIPAY.equals(channel)) {
                log.info("开始支付宝退款orderParkingId:" + orderParking.getId());
                String tradeNo = orderParking.getTradeNo();
                payService.alipayTradeRefund(tradeNo, realFee, serialNumber);
            } else if (Constants.PAY_CHANNEL_WXPAY.equals(channel) || Constants.PAY_CHANNEL_WXPAY_PUBLIC.equals(channel) || Constants.PAY_CHANNEL_WXPAY_XCX.equals(channel)) {
                log.info("开始微信退款orderParkingId:" + orderParking.getId());
                String tradeNo = orderParking.getTradeNo();
                payService.wxpayRefund(tradeNo, realFee, realFee, serialNumber, channel);
            }
            //更新分成并修改支付方式
            orderParkingRepository.updateIncomeAndPayChannel(orderId, Constants.BIGDECIMAL_ZERO, Constants.BIGDECIMAL_ZERO, Constants.BIGDECIMAL_ZERO, Constants.PAY_CHANNEL_NONE);
            //插入退款表
            OrderRefund orderRefund = new OrderRefund(serialNumber, orderId, orderParking.getTradeNo(), orderParking.getRealFee(), Status.Refund.PROCESSING.getInt(), now, now, orderParking.getPayChannel());
            orderRefundRepository.save(orderRefund);
            //保存到订单总表
            OrderTotal orderTotal = new OrderTotal(userId, serialNumber, Constants.REFUND_ORDER, now, refundFee, Status.Refund.PROCESSING.getInt());
            orderTotalRepository.save(orderTotal);
        } else {
            if ((null != realFee && realFee.compareTo(Constants.BIGDECIMAL_ZERO) > 0)) {
                //增加用户的可开票金额
                balanceUserRepository.updateBalanceInvoice(realFee, userId);
                //更改订单的可开票状态
                orderParkingRepository.updateInvoiceState(orderId, Status.InvoiceStatus.UNMAKE.getInt());
            }
            BigDecimal ownerIncome = orderParking.getOwnerIncome();
            BigDecimal manageIncome = orderParking.getManageIncome();
            //更新车位所有者分成
            balanceUserRepository.updateBalanceEarnByParklocId(parklocId, ownerIncome);
            //更新停车场分成
            balanceParklotRepository.updateBalanceByParklotId(orderParking.getParklotId(), manageIncome);
        }
        if ((null == realFee || realFee.equals(Constants.BIGDECIMAL_ZERO)) && !Constants.PAY_CHANNEL_COUPON.equals(orderParking.getPayChannel())) {
            orderParkingRepository.updatePayChannel(orderId, Constants.PAY_CHANNEL_NONE);
        }
        Integer parklotId = orderParking.getParklotId();
        Parklot parklot = parklotRepository.findOne(parklotId);
        Integer barrierManufacturer = parklot.getBarrierManufacturer();
        //删除白名单
        if (Constants.BARRIER_MANUFACTURER_BOOSTED_GOAL.equals(barrierManufacturer)) {
            this.registeredTemp(orderParking);
        }

        //线程处理待取消待发布
        orderFinishPublishTask(orderParking);
        return RespUtil.successResp(refundFee);
    }


    /**
     * 临时卡登记功能
     *
     * @param orderParking 预约订单
     */
    private void registeredTemp(OrderParking orderParking) {
        try {
            log.info("零时卡登记orderParkingId:{}", orderParking.getId());
            Integer plateId = orderParking.getPlateId();
            Integer parklotId = orderParking.getParklotId();
            Plate plate = plateRepository.findOne(plateId);
            Long now = System.currentTimeMillis();
            List<String> params = new ArrayList<>();
            String accessCode = configuration.getBoostedGoalAccessCode();
            String plateNumber = plate.getNumber();
            String beginDate = TimeUtil.timestampToStr(now);
            params.add("AccessCode=" + accessCode);
            params.add("BusinessCode=" + "RP0001");
            params.add("SignType=" + "SHAONE");
            params.add("PlateNumber=" + plateNumber);
            //查询车场在对应表中的Id
            Parklot parklot = parklotRepository.findOne(parklotId);
            String extraParklotId = parklot.getExtraParklotId();
            log.info("extraParklotId:{}", extraParklotId);
            params.add("ParkingID=" + extraParklotId);
            params.add("BeginDate=" + beginDate);
            String endDate = TimeUtil.timestampToStr(now + 1000);
            params.add("EndDate=" + endDate);
            params.add("Secretkey=" + configuration.getBoostedGoalSecretKey());
            String sign = EncryptUtil.signature(params);
            BoostedGoalParkingRequest request = new BoostedGoalParkingRequest(accessCode, "RP0001", "SHAONE", sign, plateNumber, parklot.getExtraParklotId().toString(), beginDate, endDate);
            Gson gson = new Gson();
            String json = "data=" + gson.toJson(request);
            String url = configuration.getBoostedGoalParkingUrl();
            barrierApiService.registeredTemp(json, url, Constants.APPLICATION_FORM);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp saveSelect(SaveSelectRequest saveSelectRequest) {
        //验证时间戳是否合法
        if (CommonUtil.isTimeStampInValid(saveSelectRequest.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        Integer userId = saveSelectRequest.getMobileUserId();
        Integer parklotId = saveSelectRequest.getParklotId();
        Long startTime = saveSelectRequest.getStartTime();
        Long endTime = saveSelectRequest.getEndTime();
        ReserveFilter reserveFilter = new ReserveFilter();
        reserveFilter.setMobileUserId(userId);
        reserveFilter.setParklotId(parklotId);
        reserveFilter.setStartTime(startTime);
        reserveFilter.setEndTime(endTime);
        reserveFilterRepository.save(reserveFilter);
        return RespUtil.successResp();
    }

    @Async
    public void orderFinishPublishTask(OrderParking orderParking) {
        publishService.dealWithToBePublish(orderParking);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Resp saveOrderParking(OrderParkingRequest orderParkingRequest) {
        //验证时间戳是否合法
        if (CommonUtil.isTimeStampInValid(orderParkingRequest.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        List<Integer> states = new ArrayList<>();
        states.add(Status.OrderParking.USED.getInt());
        states.add(Status.OrderParking.UNPAID.getInt());
        List<OrderParking> parkingList = orderParkingRepository.findOrderParkingByParklotInfo(orderParkingRequest.getReserveId(), states);
        if (parkingList != null && parkingList.size() > Constants.EMPTY_CAPACITY) {
            throw new QhieException(Status.ApiErr.REPEAT_PARKING);
        }
        OrderParking reserveOrder = orderParkingRepository.findOne(orderParkingRequest.getReserveId());
        Long enterTime = System.currentTimeMillis();
        BigDecimal initialMoney = new BigDecimal(0.00);
        OrderParking orderParkings = new OrderParking();
        orderParkings.setReserveId(reserveOrder.getId());
        orderParkings.setSerialNumber(OrderNoGenerator.getOrderNo(Status.OrderType.TYPE_PARKING.getInt(), String.valueOf(reserveOrder.getMobileUserId())));
        orderParkings.setMobileUserId(reserveOrder.getMobileUserId());
        orderParkings.setReservationId(reserveOrder.getReservationId());
        orderParkings.setParklocId(reserveOrder.getParklocId());
        orderParkings.setParklotId(reserveOrder.getParklotId());
        orderParkings.setPlateId(reserveOrder.getPlateId());
        orderParkings.setRealStartTime(null);
        orderParkings.setTotalFee(initialMoney);
        orderParkings.setDiscountFee(initialMoney);
        orderParkings.setRealFee(initialMoney);
        orderParkings.setTripartiteFee(initialMoney);
        orderParkings.setPlatformIncome(initialMoney);
        orderParkings.setOwnerIncome(initialMoney);
        orderParkings.setManageIncome(initialMoney);
        orderParkings.setCreateTime(System.currentTimeMillis());
        orderParkings.setState(Status.OrderParking.USED.getInt());
        orderParkings.setOvertimeFee(Constants.BIGDECIMAL_ZERO);
        log.info("save the reserve order");
        OrderParking orderParkingResp = orderParkingRepository.save(orderParkings);
        if (null == orderParkingResp) {
            throw new QhieException(Status.ApiErr.INSERT_ERROR);
        }
        //保存到订单总表
        OrderTotal orderTotal = new OrderTotal(reserveOrder.getMobileUserId(), orderParkings.getSerialNumber(), Constants.PARKING_ORDER, reserveOrder.getCreateTime(), Constants.BIGDECIMAL_ZERO, Status.OrderParking.USED.getInt());
        orderTotalRepository.save(orderTotal);
        OrderParkingLockData orderParkingLockData=new OrderParkingLockData();
        orderParkingLockData.setOrderId(orderParkingResp.getId());
        orderParkingLockData.setOrderState(orderParkingResp.getState());
        return RespUtil.successResp(orderParkingLockData);
    }
}

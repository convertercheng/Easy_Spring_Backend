package com.qhieco.apiservice.impl;

import com.qhieco.apiservice.OrderService;
import com.qhieco.apiservice.PublishService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.commonentity.*;
import com.qhieco.commonrepo.*;
import com.qhieco.commonrepo.iot.BalanceParklotRepository;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.mapper.*;
import com.qhieco.request.api.BillQueryRequest;
import com.qhieco.request.api.ReserveOrderDetailRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.api.*;
import com.qhieco.util.RespUtil;
import com.qhieco.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/2/28 16:13
 * <p>
 * 类说明：
 * 订单的service
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccessMapper accessMapper;

    @Autowired
    private LockMapper lockMapper;

    @Autowired
    private ParklotMapper parklotMapper;

    @Autowired
    private ShareMapper shareMapper;

    @Autowired
    private OrderRefundMapper orderRefundMapper;

    @Autowired
    private ParklocRepository parklocRepository;

    @Autowired
    private PlateRepository plateRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ParklotRepository parklotRepository;

    @Autowired
    private OrderParkingRepository orderParkingRepository;

    @Autowired
    private ParklotParamsBRepository parklotParamsBRepository;

    @Autowired
    private BalanceUserRepository balanceUserRepository;

    @Autowired
    private PublishService publishService;

    @Autowired
    private BalanceParklotRepository balanceParklotRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponOrderParkingBRepository couponOrderParkingBRepository;

    @Override
    public OrderUsingRepData queryOrderUsingByUserId(Integer userId) {
        HashMap<String, Object> param = new HashMap<>(8);
        param.put("userId", userId);
        param.put("unconfirmed", Status.OrderParking.UNCONFIRMED.getInt());
        param.put("reserved", Status.OrderParking.RESERVED.getInt());
        param.put("used", Status.OrderParking.USED.getInt());
        param.put("unpaid", Status.OrderParking.UNPAID.getInt());

        OrderUsingRepData orderUsingRepData = orderMapper.queryOrderUsing(param);
        if (orderUsingRepData != null) {
            // 查询门禁列表
            List<AccessRespData> accessList = accessMapper.queryAccessListByOrderId(orderUsingRepData.getOrderId(), Status.Common.VALID.getInt());
            orderUsingRepData.setAccessList(accessList);
        }
        log.info("查询用户正在使用中状态的订单，params = " + param + ", orderUsingRepData = " + orderUsingRepData);
        return orderUsingRepData;
    }

    @Override
    public List<BillRepData> queryBillListByUserId(Integer userId, int pageNum, String type, String date) {
        List<BillRepData> billRepDataList = new ArrayList<BillRepData>();

        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("startPage", pageNum < 0 ? 0 : pageNum * Constants.PAGE_SIZE);
        params.put("pageSize", Constants.PAGE_SIZE);
        // 预约类型
        params.put("resereType", Status.OrderType.TYPE_RESERVE.getInt());
        // 停车类型
        params.put("parkingType", Status.OrderType.TYPE_PARKING.getInt());
        // 停车订单状态
        params.put("paid", Status.OrderParking.PAID.getInt());
        // 预约订单状态
        params.put("reserved", Status.OrderParking.RESERVED.getInt());
        // 超时状态
        params.put("timeout", Status.OrderParking.TIMEOUT.getInt());
        // 退款订单状态
        params.put("refund", Status.Refund.PROCESS_SUCCESS_TOTAL.getInt());
        // 提现订单状态
        params.put("withdraw", Status.Withdraw.PROCESS_SUCCESS.getInt());
//        params.put("valid", Status.Common.VALID.getInt());
        // 当前查询的类型
        params.put("queryType", type);
        // 优惠券完全抵扣支付
        params.put("payChannelCoupon", Constants.PAY_CHANNEL_COUPON);
        // 查询日期
        params.put("date", date);

        checkParams(params, type);
        // 查询用户类型
        int userType = userMapper.queryUserTypeByUserId(userId);
//        params.put("userType", userType);
        log.info(" 查询用户账单列表 参数，params = " + params);

        // modify2018年5月28日 账单列表只显示金额大于0 的账单

        if (userType == Constants.PARKING_RENTEE && params.containsKey("income")) {
            log.error("普通车主用户不能查询收费类型账单，返回空");

        }else if(userType == Constants.PARKING_ADMIN &&
                (BillQueryRequest.TYPE_RESERVE_CONSUME.equals(type) || BillQueryRequest.TYPE_PARKING_CONSUME.equals(type))){
            log.error(" 管理员用户不能查询消费类型账单， 返回空");

        }else if(userType == Constants.PARKING_RENTEE){
            log.info("查询车主的账单列表");
            billRepDataList = orderMapper.queryCarOwnerBillListByUserId(params);
        }else if(userType == Constants.PARKING_RENTER){
            log.info("查询业主的账单列表");
            billRepDataList = orderMapper.queryOwnerBillListByUserId(params);
        }else if (userType == Constants.PARKING_ADMIN){
            log.info("查询管理员的账单列表");
            billRepDataList = orderMapper.queryAdminBillListByUserId(params);
        }
        return billRepDataList;
    }

    private void checkParams(HashMap<String, Object> params, String type) {
        // 预约消费类型
        if (BillQueryRequest.TYPE_RESERVE_CONSUME.equals(type)) {
            params.put("type", Status.OrderType.TYPE_RESERVE.getInt());
            // 停车消费类型
        } else if (BillQueryRequest.TYPE_PARKING_CONSUME.equals(type)) {
            params.put("type", Status.OrderType.TYPE_PARKING.getInt());
            // 退款类型
        } else if (BillQueryRequest.TYPE_REFUND.equals(type)) {
            params.put("type", Status.OrderType.TYPE_REFUND.getInt());
            // 提现类型
        } else if (BillQueryRequest.TYPE_WITHDRAW.equals(type)) {
            params.put("type", Status.OrderType.TYPE_WITHDRAW.getInt());
            // 预约收费类型
        } else if (BillQueryRequest.TYPE_RESERVE_CHARGE.equals(type)) {
            params.put("type", Status.OrderType.TYPE_RESERVE.getInt());
            params.put("income", Status.Common.VALID.getInt());
            // 停车收费类型
        } else if (BillQueryRequest.TYPE_PARKING_CHARGE.equals(type)) {
            params.put("type", Status.OrderType.TYPE_PARKING.getInt());
            params.put("income", Status.Common.VALID.getInt());
        }
    }

    @Override
    public BillDetailRepData queryBillDetailBySerialNumber(String serialNumber, Integer type, Integer userId) {
        Integer userType = null;
        if (!StringUtils.isEmpty(userId)) {
            userType = userMapper.queryUserTypeByUserId(userId);
        }
        return orderMapper.queryBillDetailBySerialNumber(serialNumber, type, userType);
    }

    @Override
    public List<InvoiceOrderRepData> queryInvoiceOrderListByCondition(Integer userId, int pageNum) {
        int startPage = pageNum < 0 ? 0 : pageNum * Constants.PAGE_SIZE;
        return orderMapper.queryInvoiceOrderListByCondition(userId,
                Status.InvoiceStatus.UNMAKE.getInt(), startPage, Constants.PAGE_SIZE);
    }

    @Override
    public ReserveOrderDetailRespData queryReserveOrderDetail(Integer orderId) {
        ReserveOrderDetailRespData reserveOrderDetailRespData = new ReserveOrderDetailRespData();
        OrderParking orderParking;
        if (null == (orderParking = orderParkingRepository.findOne(orderId))) {
            throw new QhieException(Status.ApiErr.NONEXISTENT_ORDER_PARKING);
        }
        setBaseInfo(orderId, reserveOrderDetailRespData, orderParking);
        setExtraInfo(reserveOrderDetailRespData, orderParking);
        setIotInfo(orderId, reserveOrderDetailRespData);
        return reserveOrderDetailRespData;
    }

    @Override
    public Resp queryReserveOrderDetail(ReserveOrderDetailRequest reserveOrderDetailRequest) {
        ReserveOrderDetailRespData reserveOrderDetailRespData = new ReserveOrderDetailRespData();
        OrderParking orderParking;
        final Integer orderId = reserveOrderDetailRequest.getOrder_id();
        if (null == (orderParking = orderParkingRepository.findOne(orderId))) {
            throw new QhieException(Status.ApiErr.NONEXISTENT_ORDER_PARKING);
        }
        Integer state = orderParking.getState();
        if (orderParking.getReserveId() == null) {
            Integer parkingState = orderParkingRepository.findParkingStateByReserveId(orderParking.getId());
            if (!StringUtils.isEmpty(parkingState)) {
                state = parkingState;
                log.info("该预约订单已进入停车流程， state = " + state);
            }
        }

        if (Status.OrderParking.USED.getInt().equals(state) ||Status.OrderParking.UNPAID.getInt().equals(state) ||
                Status.OrderParking.PAID.getInt().equals(state) || Status.OrderParking.UNCONFIRMED.getInt().equals(state) ||
        Status.OrderParking.SYS_CANCELED.getInt().equals(state) || Status.OrderParking.TIMEOUT.getInt().equals(state) ||
                Status.OrderParking.USER_CANCELED.getInt().equals(state) || Status.OrderParking.CUST_SERVICE_CANCELED.getInt().equals(state)) {
            setBaseInfo(orderId, reserveOrderDetailRespData, orderParking);
            setExtraInfo(reserveOrderDetailRespData, orderParking);
            setIotInfo(orderId, reserveOrderDetailRespData);
            return RespUtil.successResp(reserveOrderDetailRespData);
        } else {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                log.error("线程异常，" + e);
            }
            return RespUtil.successResp();
        }
    }

    /**
     * 设置车锁门禁等信息
     * @param orderId 订单id
     * @param reserveOrderDetailRespData 返回数据
     */
    private void setIotInfo(Integer orderId, ReserveOrderDetailRespData reserveOrderDetailRespData) {
        List<AccessRespData> accessList = accessMapper.queryAccessListByOrderId(orderId, Status.Common.VALID.getInt());
        reserveOrderDetailRespData.setAccessList(accessList);
        // 查询车位锁信息
        HashMap lockMap = lockMapper.queryLockInfoByParklocId(orderId, Status.Common.VALID.getInt());
        if (lockMap != null) {
            reserveOrderDetailRespData.setLockBtName(lockMap.get("btName") != null ? lockMap.get("btName").toString() : "");
            reserveOrderDetailRespData.setLockBtPwd(lockMap.get("btPwd") != null ? lockMap.get("btPwd").toString() : "");
            reserveOrderDetailRespData.setLockType(lockMap.get("lockType") != null
                    ? Integer.valueOf(lockMap.get("lockType").toString()) : null);
            if (lockMap.get("lockId") != null) {
                reserveOrderDetailRespData.setLockId(Integer.valueOf(lockMap.get("lockId").toString()));
            }
        }

    }

    /**
     * 设置订单的一些其他信息
     * @param reserveOrderDetailRespData 返回数据
     * @param orderParking 订单
     */
    private void setExtraInfo(ReserveOrderDetailRespData reserveOrderDetailRespData, OrderParking orderParking) {
        Integer parklotId = orderParking.getParklotId();
        Parklot parklot = parklotRepository.findOne(parklotId);
        reserveOrderDetailRespData.setParklotId(parklotId);
        reserveOrderDetailRespData.setParklotName(parklot.getName());
        reserveOrderDetailRespData.setParklotKind(parklot.getKind());
        reserveOrderDetailRespData.setType(parklot.getType());
        reserveOrderDetailRespData.setChargeType(parklot.getChargeType());
        reserveOrderDetailRespData.setAddress(parklot.getAddress());
        reserveOrderDetailRespData.setLat(parklot.getNaviLat());
        reserveOrderDetailRespData.setLng(parklot.getNaviLng());
        Integer parklocId = orderParking.getParklocId();
        Parkloc parkloc = parklocRepository.findOne(parklocId);
        reserveOrderDetailRespData.setParklocNumber(parkloc.getNumber());
        Integer plateId = orderParking.getPlateId();
        Plate plate = plateRepository.findOne(plateId);
        reserveOrderDetailRespData.setPlateNo(plate.getNumber());
        String minChargePeriod = parklotParamsBRepository.findValueByParklotId(parklotId, Constants.MIN_CHARGING_PERIOD, Status.Common.VALID.getInt());
        if(StringUtils.isEmpty(minChargePeriod)){
            minChargePeriod = Constants.MIN_CHARGING_PERIOD_DEFAULT;
        }
        reserveOrderDetailRespData.setMinChargingPeriod(Integer.valueOf(minChargePeriod));
        /*FeeRuleParking feeRuleParking = parklotMapper.parklotParkingFeeRule(parklotId);
        ParkingFeeData parkingFeeData = new ParkingFeeData();
        parkingFeeData.setId(feeRuleParking.getId());
        if (TimeUtil.isWeekDay(System.currentTimeMillis())) {
            parkingFeeData.setFirstHourFee(new BigDecimal(feeRuleParking.getFirstHourFee().split(Constants.DELIMITER_COMMA)[1]));
            parkingFeeData.setOtherTimeFee(new BigDecimal(feeRuleParking.getOtherTimeFee().split(Constants.DELIMITER_COMMA)[1]));
            parkingFeeData.setMaxFee(new BigDecimal(feeRuleParking.getMaxFee().split(Constants.DELIMITER_COMMA)[1]));
            parkingFeeData.setFreeUseTime(Integer.valueOf(feeRuleParking.getFreeUseTime().split(Constants.DELIMITER_COMMA)[1]));
            parkingFeeData.setOverTimeFee(new BigDecimal(feeRuleParking.getOverTimeFee().split(Constants.DELIMITER_COMMA)[1]));
            parkingFeeData.setWeekday(Constants.DayType.WEEKDAY.ordinal());
        } else {
            parkingFeeData.setFirstHourFee(new BigDecimal(feeRuleParking.getFirstHourFee().split(Constants.DELIMITER_COMMA)[0]));
            parkingFeeData.setOtherTimeFee(new BigDecimal(feeRuleParking.getOtherTimeFee().split(Constants.DELIMITER_COMMA)[0]));
            parkingFeeData.setMaxFee(new BigDecimal(feeRuleParking.getMaxFee().split(Constants.DELIMITER_COMMA)[0]));
            parkingFeeData.setFreeUseTime(Integer.valueOf(feeRuleParking.getFreeUseTime().split(Constants.DELIMITER_COMMA)[0]));
            parkingFeeData.setOverTimeFee(new BigDecimal(feeRuleParking.getOverTimeFee().split(Constants.DELIMITER_COMMA)[0]));
            parkingFeeData.setWeekday(Constants.DayType.WEEKEND.ordinal());
        }
        reserveOrderDetailRespData.setParkingFeeData(parkingFeeData);*/
    }

    /**
     * 设置基本订单信息
     * @param orderId 订单id
     * @param reserveOrderDetailRespData 返回数据
     * @param orderParking 订单
     */
    private void setBaseInfo(Integer orderId, ReserveOrderDetailRespData reserveOrderDetailRespData, OrderParking orderParking) {
        reserveOrderDetailRespData.setOrderId(orderId);
        reserveOrderDetailRespData.setSerialNumber(orderParking.getSerialNumber());
        reserveOrderDetailRespData.setState(orderParking.getState());
        reserveOrderDetailRespData.setCreateTime(orderParking.getCreateTime());
        Integer reservationId = orderParking.getReservationId();
        Reservation reservation = reservationRepository.findOne(reservationId);
        reserveOrderDetailRespData.setStartTime(reservation.getStartTime());
        reserveOrderDetailRespData.setEndTime(reservation.getEndTime());
        // 免费取消时长
//        String freeCancellationTime = parklotParamsBRepository.findValueByParklotId(orderParking.getParklotId(), Constants.FREE_CANCELLATION_TIME, Status.Common.VALID.getInt());
        Integer freeCancellationTimeI = parklotMapper.queryParklotParamsValueByParklotId(orderParking.getParklotId(), Status.Common.VALID.getInt(), Constants.FREE_CANCELLATION_TIME);
        String freeCancellationTimeStr = "";
        if (StringUtils.isEmpty(freeCancellationTimeI)) {
            freeCancellationTimeStr = Constants.FREE_CANCELLATION_TIME_DEFAULT;
        } else {
            freeCancellationTimeStr = String.valueOf(freeCancellationTimeI);
        }
        reserveOrderDetailRespData.setFreeCancellationTime(freeCancellationTimeStr);
        long now = System.currentTimeMillis();
        Coupon coupon = orderMapper.queryCouponByOrderId(orderId);
        // 停车订单
        if (null != orderParking.getReserveId()) {
            // 查询停车订单对应的预约订单
            OrderParking reserveOrder = orderParkingRepository.findOne(orderParking.getReserveId());
            // 计算需要支付的停车费
            BigDecimal payFee;
            if (Status.PaidFlag.OFF_LINE_PAID.getInt().equals(orderParking.getParkingFeePaidFlag())) {
                payFee = orderParking.getOvertimeFee();
            } else {
                payFee = orderParking.getTotalFee().add(orderParking.getOvertimeFee());
            }
            reserveOrderDetailRespData.setPayFee(payFee);
            reserveOrderDetailRespData.setTotalFee(reserveOrder.getTotalFee().add(orderParking.getTotalFee()).add(orderParking.getOvertimeFee()));
            reserveOrderDetailRespData.setReserveFee(reserveOrder.getTotalFee());
            reserveOrderDetailRespData.setParkingFee(orderParking.getTotalFee());
            reserveOrderDetailRespData.setPayChannelParking(orderParking.getPayChannel());
            reserveOrderDetailRespData.setPayChannelReserve(reserveOrder.getPayChannel());
            reserveOrderDetailRespData.setParkingTradeNo(orderParking.getTradeNo());
            reserveOrderDetailRespData.setReserveTradeNo(reserveOrder.getTradeNo());
            reserveOrderDetailRespData.setOverTime(orderParking.getOvertime());
            reserveOrderDetailRespData.setOverTimeFee(orderParking.getOvertimeFee());
            reserveOrderDetailRespData.setStopTime(orderParking.getRealStartTime() != null && now - orderParking.getRealStartTime() > 0
                    ? now - orderParking.getRealStartTime() : 0);
            Coupon reserveCoupon = orderMapper.queryCouponByOrderId(reserveOrder.getId());
            if (null != reserveCoupon) {
                reserveOrderDetailRespData.setReserveCouponFee(reserveCoupon.getUsedMoney());
                reserveOrderDetailRespData.setReserveCouponCode(reserveCoupon.getCouponCode());
            }
            if (null != coupon) {
                reserveOrderDetailRespData.setParkingCouponCode(coupon.getCouponCode());
                reserveOrderDetailRespData.setParkingCouponFee(coupon.getUsedMoney());
            }

            // 计算停车费支付明细
            // 计算线上第三方支付停车费金额
            BigDecimal parkingFeeOnLine = BigDecimal.ZERO;
            Integer parkingFeePaidFlag = orderParking.getParkingFeePaidFlag();
            if (Status.PaidFlag.OFF_LINE_PAID.getInt().equals(parkingFeePaidFlag)) {
                // 线下已支付：支付金额=超时费-优惠金额
                parkingFeeOnLine = reserveOrderDetailRespData.getOverTimeFee().
                        subtract(reserveOrderDetailRespData.getParkingCouponFee() == null ? BigDecimal.ZERO : reserveOrderDetailRespData.getParkingCouponFee());
                // 线下支付停车费金额
                reserveOrderDetailRespData.setParkingFeeOffLine(orderParking.getTotalFee());
            } else {
                // 线下未支付: 支付金额 = 实际支付金额
                parkingFeeOnLine = orderParking.getRealFee();
            }
            reserveOrderDetailRespData.setParkingFeeOnLine(parkingFeeOnLine);

            // 预约订单
        } else {
            // 计算需要支付的停车费
            reserveOrderDetailRespData.setPayFee(orderParking.getTotalFee());
            reserveOrderDetailRespData.setTotalFee(orderParking.getTotalFee());
            reserveOrderDetailRespData.setReserveFee(orderParking.getTotalFee());
            reserveOrderDetailRespData.setPayChannelReserve(orderParking.getPayChannel());
            reserveOrderDetailRespData.setReserveTradeNo(orderParking.getTradeNo());
//            String maxDelayTimeStr = parklotParamsBRepository.findValueByParklotId(orderParking.getParklotId(), Constants.MAX_DELAY_TIME, Status.Common.VALID.getInt());
            Integer maxDelayTimeI = parklotMapper.queryParklotParamsValueByParklotId(orderParking.getParklotId(), Status.Common.VALID.getInt(), Constants.MAX_DELAY_TIME);
            log.info("苗费");
            if (StringUtils.isEmpty(maxDelayTimeI)) {
                maxDelayTimeI = Integer.valueOf(Constants.MAX_DELAY_TIME_DEFAULT);
            }
            Long maxDelayTime = TimeUtil.minutesToMilliSeconds(maxDelayTimeI);
            reserveOrderDetailRespData.setEnterCountdownTime(reservation.getStartTime() + maxDelayTime - now > 0 ? reservation.getStartTime() + maxDelayTime - now : 0);
            if (null != coupon) {
                reserveOrderDetailRespData.setReserveCouponFee(coupon.getUsedMoney());
                reserveOrderDetailRespData.setReserveCouponCode(coupon.getCouponCode());
            }

            // 查询该预约订单是否有对应的停车订单状态
            HashMap parkingOrderMap = orderMapper.queryParkingStateByReserveId(orderParking.getId());
            if (parkingOrderMap != null && parkingOrderMap.containsKey("id") && parkingOrderMap.containsKey("state")) {
                reserveOrderDetailRespData.setOrderParkingId(Integer.valueOf(parkingOrderMap.get("id").toString()));
                reserveOrderDetailRespData.setParkingState(Integer.valueOf(parkingOrderMap.get("state").toString()));
            }
        }
        reserveOrderDetailRespData.setEnterTime(orderParking.getRealStartTime());
        reserveOrderDetailRespData.setLeaveTime(orderParking.getRealEndTime());
    }

    @Override
    public List<ReserveOrderRespData> queryReserveList(Integer userId, Integer pageNum) {

        Integer startPage = pageNum * Constants.PAGE_SIZE;
        log.info("查询预约列表参数，userId = " + userId + ", startPage = " + startPage);
        List<ReserveOrderRespData> reserveList = orderMapper.queryReserveOrderListByUserId(userId, startPage, Constants.PAGE_SIZE);
        return reserveList;
    }

    @Override
    public List<OrderManageRespData> queryOrderManageList(Integer userId, Integer pageNum) {
        int startPage = pageNum * Constants.PAGE_SIZE;
        int userType = userMapper.queryUserTypeByUserId(userId);
        if (userType == Constants.PARKING_RENTEE) {
            log.error("车主没有权限访问订单管理列表");
            return null;
        }
        HashMap<String, Object> params = new HashMap<>(8);
        params.put("userId", userId);
        params.put("startPage", startPage);
        params.put("pageSize", Constants.PAGE_SIZE);

        log.info(" 查询订单管理列表 参数 params= " + params + ", userType = " + userType);

        List<OrderManageRespData> dataList = null;
        if (Constants.PARKING_RENTER == userType) {
            log.info("业主");
            dataList = orderMapper.queryOwnerOrderManageListByCondition(params);
        } else if (Constants.PARKING_ADMIN == userType) {
            log.info("管理员");
            dataList = orderMapper.queryManagerOrderManageListByCondition(params);
        }
        return dataList;
    }

    @Override
    public OrderManageDetailRespData queryOrderManageDetail(Integer userId, Integer orderId) {
        int userType = userMapper.queryUserTypeByUserId(userId);
        if (userType == Constants.PARKING_RENTEE) {
            log.error("车主没有权限访问订单详情");
            return null;
        }
        OrderManageDetailRespData orderManageDetailRespData = orderMapper.queryOrderManageDetailByOrderId(userType, orderId);
        return orderManageDetailRespData;
    }

    /**
     * 对未确认订单进行超时处理
     * @param orderId
     * @param shareId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUnconfirmedOrderTimeout(int orderId, int shareId) {
        // 更新share状态为可用
        shareMapper.updateShareStateById(shareId, Status.Common.VALID.getInt());
        // 更新车位状态为已发布
        parklotMapper.updateParklocStateByOrderId(orderId, Status.Parkloc.PUBLISHED.getInt());
        // 更新订单状态为超时
        orderMapper.updateOrderStateById(orderId, Status.OrderParking.TIMEOUT.getInt());
        //更新order_total表的状态为超时
        orderMapper.updateOrderTotalStateByOrderId(orderId, Status.OrderParking.TIMEOUT.getInt());
        // 更新订单支付渠道为0
        orderParkingRepository.updatePayChannel(orderId, Constants.PAY_CHANNEL_NONE);
        //需要返还优惠券
        Coupon coupon = couponRepository.findCouponByOrderId(orderId);
        if(null != coupon ) {
            log.info("订单关联有优惠券，解除此优惠券绑定， " + coupon);
            Integer couponId = coupon.getId();
            //删除关联关系
            couponOrderParkingBRepository.cancel(orderId, couponId, Status.Common.DELETED.getInt(), System.currentTimeMillis());
            //如果优惠券已经使用了，改变优惠券状态
            if (coupon.getState().equals(Status.Coupon.USED.getInt())) {
                //优惠券置为有效状态
                couponRepository.updateStateAndUseTimeAndUseMoneyById(couponId, Status.Coupon.COUPON_CONVERTIBILITY.getInt(), null, Constants.BIGDECIMAL_ZERO);
                log.info("返还优惠券couponId:" + couponId);
            }
        }
        // 处理 待修改  待取消 的车位信息
        OrderParking orderParking = orderParkingRepository.findOne(orderId);
        publishService.dealWithToBePublish(orderParking);
    }


    /***
     * 对已预约未进场订单进行超时处理
     * @param orderId 订单id
     * @param shareId 分享id
     * @param ownerIncome 业主收入
     * @param manageIncome 物业收入
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateReservedOrderTimeout(int orderId, int shareId, BigDecimal ownerIncome, BigDecimal manageIncome) {
        // 更新share状态为可用
        shareMapper.updateShareStateById(shareId, Status.Common.VALID.getInt());
        // 更新车位状态为已发布
        parklotMapper.updateParklocStateByOrderId(orderId, Status.Parkloc.PUBLISHED.getInt());
        // 更新订单状态为超时 && 订单开票状态为可开票 && 更新用户的可开票金额
        orderMapper.updateOrderStateAdInvoiceStateById(orderId, Status.OrderParking.TIMEOUT.getInt(), Status.InvoiceStatus.UNMAKE.getInt());
        HashMap<String, Object> invoiceMap = orderMapper.queryInvoiceInfoByOrderId(orderId);
        if (invoiceMap != null && invoiceMap.containsKey("realFee") && invoiceMap.containsKey("userId")) {
            log.info("更新用户的可开票金额 invoiceMap = " + invoiceMap);
            Number userId = (Long) invoiceMap.get("userId");
            balanceUserRepository.updateBalanceInvoice(new BigDecimal(invoiceMap.get("realFee").toString()), userId.intValue());
        }
        //更新order_total表的状态为超时
        orderMapper.updateOrderTotalStateByOrderId(orderId, Status.OrderParking.TIMEOUT.getInt());
        // 更新业主余额
        userMapper.updateOwnerBalanceByOrderId(orderId, ownerIncome);
        // 更新管理员余额
        HashMap<String, Integer> mapInfo = orderMapper.queryManageUserIdAdParklotIdByOrderId(orderId);
        Number manageUserId = mapInfo.get("userId");
        Number parklotId = mapInfo.get("parklotId");
        userMapper.updateManageBalanceByOrderId(manageUserId.intValue(), manageIncome);
        balanceParklotRepository.updateBalanceByParklotId(parklotId.intValue(), manageIncome);

        // 处理 待修改  待取消 的车位信息
        OrderParking orderParking = orderParkingRepository.findOne(orderId);
        publishService.dealWithToBePublish(orderParking);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderRefundAndOrderTotal(String serialNumber, int state) {
        orderRefundMapper.updateOrderRefundStateBySerialNumber(serialNumber, state);
        orderMapper.updateOrderTotalStateBySerialNumber(serialNumber, state);
    }


    @Async
    public void orderFinishPublishTask(OrderParking orderParking)  {
        publishService.dealWithToBePublish(orderParking);
    }

}

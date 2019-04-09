package com.qhieco.apiservice.impl;

import com.qhieco.apiservice.OrderChargingService;
import com.qhieco.apiservice.ParklotAmountService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.common.QhieCurrency;
import com.qhieco.commonentity.*;
import com.qhieco.commonentity.iotdevice.Lock;
import com.qhieco.commonrepo.*;
import com.qhieco.commonrepo.iot.BalanceParklotRepository;
import com.qhieco.commonrepo.iot.LockRepository;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.mapper.FeeRuleParkingMapper;
import com.qhieco.mapper.ParklotParamsMapper;
import com.qhieco.mapper.ShareMapper;
import com.qhieco.push.QhMessageTemplate;
import com.qhieco.push.QhMessageType;
import com.qhieco.request.api.ParkingRuleData;
import com.qhieco.response.Resp;
import com.qhieco.response.data.api.OrderParkingData;
import com.qhieco.response.data.api.OrderParkingLockData;
import com.qhieco.response.data.web.ParkingFeeRuleInfoData;
import com.qhieco.util.*;
import com.qhieco.websocket.CustomWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018-06-26 18:53
 * <p>
 * 类说明：
 * ${description}
 */
@Service
@Slf4j
public class OrderChargingServiceImpl implements OrderChargingService {
    @Autowired
    private OrderParkingRepository orderParkingRepository;


    @Autowired
    private UserExtraInfoRepository userExtraInfoRepository;

    @Autowired
    private LockRepository lockRepository;


    @Autowired
    private IntegralRepository integralRepository;

    @Autowired
    private UserMobileRepository userMobileRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ParklotParamsMapper parklotParamsMapper;

    @Autowired
    private ParklocRepository parklocRepository;

    @Autowired
    private ShareRepository shareRepository;

    @Autowired
    private ParklotAmountService parklotAmountService;

    @Autowired
    private PublishRepository publishRepository;

    @Autowired
    private ShareMapper shareMapper;

    @Autowired
    private ParklotParamsBRepository parklotParamsBRepository;

    @Autowired
    private BalanceUserRepository balanceUserRepository;

    @Autowired
    private BalanceParklotRepository balanceParklotRepository;

    @Autowired
    private ParklotRepository parklotRepository;

    @Autowired
    private OrderTotalRepository orderTotalRepository;

    @Autowired
    private FeeRuleParkingMapper feeRuleMapper;

    private static final String ENTER_TAG = "enter";

    private static final String EXIT_TAG = "exit";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp process(Integer tag, Integer parklotId, Integer parklocId) throws Exception {
        log.info("parklot:{}", parklotId);
        log.info("tag is {}", tag);
        log.info("parkloc:{}", parklocId);
        Parklot parklot = parklotRepository.findOne(parklotId);
        Parkloc parkloc = parklocRepository.findOne(parklocId);
        if (Constants.TAG_LOCK_ENTER.equals(tag)) {
            log.info("进场");
            return orderCharging(parklot, parkloc);
        } else if (Constants.TAG_LOCK_LEAVE.equals(tag)) {
            log.info("出场");
            return updateParkingOrder(parklot, parkloc);
        }
        throw new QhieException(Status.ApiErr.NONEXISTENT_TAG_EPATIC_WEBHOOK);
    }

    /**
     * 车锁计费生成订单
     *
     * @param parklot
     * @param parkloc
     * @return Resp
     * @throws Exception
     */
    private Resp orderCharging(Parklot parklot, Parkloc parkloc) throws Exception {
        if (null == parklot) {
            throw new QhieException(Status.ApiErr.NONEXISTENT_PARKLOT);
        }
        if (null == parkloc) {
            throw new QhieException(Status.ApiErr.NONEXISTENT_PARKLOC);
        }
        List<Integer> states = new ArrayList<>();
        states.add(Status.OrderParking.USED.getInt());
        states.add(Status.OrderParking.UNPAID.getInt());
        Integer parklotId = parklot.getId();
        Integer parklocId = parkloc.getId();
        List<OrderParking> orderParkingList = orderParkingRepository.
                findOrderParkingByReservation(parklotId, parklocId, Status.OrderParking.RESERVED.getInt());
        if (orderParkingList == null || orderParkingList.size() == Constants.EMPTY_CAPACITY) {
            throw new QhieException(Status.ApiErr.NONEXISTENT_PARKLOC);
        }
        OrderParking reserveOrder = orderParkingList.get(Constants.FIRST_INDEX);
        OrderParking parkingOrders = orderParkingRepository.findByReserveId(reserveOrder.getId());
        log.info("reserve order is {}", reserveOrder);
        log.info("parking order is {}", parkingOrders);
        if (parkingOrders != null && parkingOrders.getRealStartTime() != null) {
            return null;
        }
        BigDecimal initialMoney = new BigDecimal(0.00);
        OrderParking orderParkings = new OrderParking();
        orderParkings.setReserveId(reserveOrder.getId());
        orderParkings.setSerialNumber(OrderNoGenerator.getOrderNo(Status.OrderType.TYPE_PARKING.getInt(), String.valueOf(reserveOrder.getMobileUserId())));
        orderParkings.setMobileUserId(reserveOrder.getMobileUserId());
        orderParkings.setReservationId(reserveOrder.getReservationId());
        orderParkings.setParklocId(reserveOrder.getParklocId());
        orderParkings.setParklotId(reserveOrder.getParklotId());
        orderParkings.setPlateId(reserveOrder.getPlateId());
        orderParkings.setRealStartTime(System.currentTimeMillis());
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
        OrderParkingLockData orderParkingLockData = new OrderParkingLockData();
        orderParkingLockData.setOrderId(orderParkingResp.getId());
        orderParkingLockData.setOrderState(orderParkingResp.getState());
        /*
         * 推送信息
         */
//        pushToWeApp(reserveOrder.getMobileUserId(), ENTER_TAG);
        UserExtraInfo userExtraInfo = userExtraInfoRepository.findByMobileUserId(orderParkings.getMobileUserId());
        if (null == userExtraInfo || StringUtils.isEmpty(userExtraInfo.getJpushRegId())) {
            return null;
        }
        //给前端发送自定义消息提示
        List<Lock> locks = lockRepository.findByParklocId(orderParkings.getParklocId());
        String btName = "";
        String btPassword = "";
        String btNumber = "";
        if (locks != null && locks.size() > Constants.EMPTY_CAPACITY) {
            btName = locks.get(0).getBtName();
            btPassword = locks.get(0).getBtPassword();
            btNumber = parkloc.getNumber();
        }
        QhPushUtil.getInstance().sendQhPush(userExtraInfo.getJpushRegId(), QhMessageType.CUSTOM, QhMessageTemplate.LEAVE,
                orderParkings.getId().toString(), btName, btPassword, btNumber);
        return RespUtil.successResp(orderParkings);

    }

    /**
     * 向userId推送
     */
    private void pushToWeApp(Integer userId, String msg) {
//        log.info("webSocket server push to user id {}", userId);
//        CustomWebSocket.sendMessage(userId, msg);
    }

    /**
     * 更新停车订单
     *
     * @param parklot,parkloc 出场
     */
    private Resp updateParkingOrder(Parklot parklot, Parkloc parkloc) {
        Integer parklotId = parklot.getId();
        Integer parklocId = parkloc.getId();
        Long realEndTime = System.currentTimeMillis();
        List<OrderParking> parkingList = orderParkingRepository.
                findOrderParkingByParklot(parklotId, parklocId, Status.OrderParking.USED.getInt());
        if (parkingList == null || parkingList.size() == Constants.EMPTY_CAPACITY ||
                parkingList.get(Constants.EMPTY_CAPACITY).getRealStartTime() == null) {
            log.info("未查询到对应的停车订单。" + parkingList);
            return null;
        }
        OrderParking parkingOrder = parkingList.get(Constants.FIRST_INDEX);
        log.info("parkingOrder is {}", parkingOrder);
        /** *   *   *  此段代码为测试使用我们系统的停车计费规则计算费用 start*   *   */
        OrderParkingData orderParkingData = null;
        try {
            calculateParkingTotalFee(realEndTime, parkingOrder);
            log.info("使用我们系统的停车计费规则 计算费用为 ：tempTotalFee = " + orderParkingData);
        } catch (Exception e) {
            log.error("计算停车费用异常， " + e);
            return null;
        }
        /** *   *   *  此段代码为测试使用我们系统的停车计费规则计算费用 end*   *   */
        List<Integral> integrals = integralRepository.findByIntegralCodeAndState(Constants.INTEGRAL_TIME_OUT, Status.Common.VALID.getInt());
        Integral integralTimeOutAdd = null;
        Integral integralTimeOutSubtract = null;
        if (null != integrals && integrals.size() > 0) {
            for (Integral integral : integrals) {
                if (integral.getIntegralType() == Constants.INTEGRAL_ADD) {
                    integralTimeOutAdd = integral;
                } else {
                    integralTimeOutSubtract = integral;
                }
            }
        }
        UserMobile userMobile = userMobileRepository.findOne(parkingOrder.getMobileUserId());
        Reservation reservation = reservationRepository.findOne(parkingOrder.getReservationId());
        log.info("停车费用和超时费用不为0getTotalFee {}", parkingOrder.getTotalFee());
        log.info("停车费用和超时费用不为0getOvertimeFee {}", parkingOrder.getOvertimeFee());
        /** *   *   *  此段代码为测试使用我们系统的停车计费规则计算费用 end*   *   */
        if (parkingOrder.getTotalFee().compareTo(Constants.BIGDECIMAL_ZERO) == 0
                && parkingOrder.getOvertimeFee().compareTo(Constants.BIGDECIMAL_ZERO) == 0) {
            OrderParking orderParkingResp = updateParkingState(parkingOrder.getTotalFee(), parkingOrder.getTotalFee(), realEndTime, parkingOrder, parklot.getAllocable());
            //更新预约订单的可开票状态
            Integer reserveOrderId = parkingOrder.getReserveId();
            orderParkingRepository.updateInvoiceState(reserveOrderId, Status.InvoiceStatus.UNMAKE.getInt());
            //更新用户的可开票金额（预约记录开票）
            OrderParking reserveOrder = orderParkingRepository.findOne(reserveOrderId);
            balanceUserRepository.updateBalanceInvoice(reserveOrder.getRealFee(), parkingOrder.getMobileUserId());
            //更新车位所有者分成(预约分成)
            balanceUserRepository.updateBalanceEarnByParklocId(reserveOrder.getParklocId(), reserveOrder.getOwnerIncome());
            //更新停车场分成（预约分成）
            balanceParklotRepository.updateBalanceByParklotId(reserveOrder.getParklotId(), reserveOrder.getManageIncome());
        } else {
            log.info("停车费用和超时费用不为0 {}", parkingOrder.getTotalFee());
            parkingOrder.setState(Status.OrderParking.UNPAID.getInt());
            parkingOrder.setRealEndTime(realEndTime);
            log.info("停车费用和超时费用不为0parkingOrder {}", parkingOrder);
            orderParkingRepository.save(parkingOrder);
        }
        if (null != realEndTime && realEndTime > reservation.getEndTime()) {
            if (null != integralTimeOutSubtract) {
                if (null != userMobile) {
                    Integer userIntegral = userMobile.getIntegral() - integralTimeOutSubtract.getIntegralPluses() < 0 ? 0 : userMobile.getIntegral() - integralTimeOutSubtract.getIntegralPluses();
                    log.info("扣除积分，扣除后剩余积分:{}", userIntegral);
                    userMobileRepository.updateUserMobilePlusIntegral(parkingOrder.getMobileUserId(), userIntegral);
                }
            }
        } else {
            if (null != integralTimeOutAdd) {
                if (null != userMobile) {
                    Integer userIntegral = userMobile.getIntegral() + integralTimeOutAdd.getIntegralPluses() > 100 ? 100 : userMobile.getIntegral() + integralTimeOutAdd.getIntegralPluses();
                    log.info("增加积分，增加后剩余积分:{}", userIntegral);
                    userMobileRepository.updateUserMobilePlusIntegral(parkingOrder.getMobileUserId(), userIntegral);
                }
            }
        }
        releaseParkloc(parkingOrder);
        //线程处理待取消待发布
        dealWithToBePublish(parkingOrder);
        /*
         * 推送信息
         */
        pushToWeApp(parkingOrder.getMobileUserId(), EXIT_TAG);
        UserExtraInfo userExtraInfo = userExtraInfoRepository.findByMobileUserId(parkingOrder.getMobileUserId());
        if (null == userExtraInfo || StringUtils.isEmpty(userExtraInfo.getJpushRegId())) {
            return null;
        }
        //给前端发送自定义消息提示
        List<Lock> locks = lockRepository.findByParklocId(parkingOrder.getParklocId());
        String btName = "";
        String btPassword = "";
        String btNumber = "";
        if (locks != null && locks.size() > Constants.EMPTY_CAPACITY) {
            btName = locks.get(0).getBtName();
            btPassword = locks.get(0).getBtPassword();
            btNumber = parkloc.getNumber();
        }
        QhPushUtil.getInstance().sendQhPush(userExtraInfo.getJpushRegId(), QhMessageType.CUSTOM, QhMessageTemplate.LEAVE,
                parkingOrder.getId().toString(), btName, btPassword, btNumber);
        return RespUtil.successResp(parkingOrder);

    }

    public BigDecimal calculateParkingTotalFee(Long realEndTime, OrderParking parkingOrder) {
        Integer parklotId = parkingOrder.getParklotId();
        ParkingFeeRuleInfoData infoData = feeRuleMapper.queryParkingFeeRuleById(parklotId);
        Integer type = infoData.getType();
        Long realStartTime = parkingOrder.getRealStartTime();
        //初始化方法变量类
        ParkingRuleData data = new ParkingRuleData();
        //初始化时间指针变量等于停车开始时间
        data.setTimePointer(realStartTime);
        BigDecimal overTimeFee = Constants.BIGDECIMAL_ZERO;
        Integer dayOfWeek;
        //保存当前最后一个收费规则，用于计算超时费用
        FeeRuleParking lastRule = null;
        Long overTime = null;
        List<FeeRuleParking> feeRuleParkings = infoData.getFeeRuleParkings();
        if (null != feeRuleParkings) {
            while (data.getTimePointer() < realEndTime) {
                Integer feeRuleParkingSize = feeRuleParkings.size();
                Integer i = 0;
                Boolean matchFlag = false;
                for (i = 0; i < feeRuleParkingSize; i++) {
                    //如果规则分为工作日/周末
                    if (1 == type) {
                        dayOfWeek = TimeUtil.isWeekDay(data.getTimePointer()) ? 1 : 0;
                    } else {
                        dayOfWeek = 2;
                    }
                    Integer weekEnd = feeRuleParkings.get(i).getWeekday();
                    Long startTime = feeRuleParkings.get(i).getStartTime();
                    Long endTime = feeRuleParkings.get(i).getEndTime();
                    if (!dayOfWeek.equals(weekEnd)) {
                        continue;
                    }
                    //如果startTime为NULL，为24小时的计费规则，该计费规则按照排序排在最后  当其他时间段都不符合，使用该规则(不含时间段)
                    if (null == startTime) {
                        lastRule = feeRuleParkings.get(i);
                        matchFlag= true;
                        //找到下个规则时间段的开始时间，作为本规则的结束时间
                        Long nextStartTime = this.findNextFeeRuleStartTime(realEndTime, data.getTimePointer(), infoData);
                        //免费时间
                        if (this.getFreeTime(data, feeRuleParkings.get(i), realEndTime, nextStartTime)) {
                            break;
                        }
                        //首小时费用
                        if (this.getFirstHourFee(data, feeRuleParkings.get(i), realStartTime, realEndTime, nextStartTime)) {
                            break;
                        }
                        //计算次小时费用
                        if (this.getOtherTimeFee(data, feeRuleParkings.get(i), nextStartTime, realEndTime)) {
                            break;
                        }
                    }
                    //把三个时间放到同一天,跨天就加一天
                    Long timePointerOneDay = TimeUtil.formatOneDayTime(data.getTimePointer());
                    Long startTimeOneDay = TimeUtil.formatOneDayTime(startTime);
                    Long endTimeOneDay = TimeUtil.formatOneDayTime(endTime);
                    if(startTimeOneDay >= endTimeOneDay){
                        endTimeOneDay = endTimeOneDay + Constants.TIMESTAMP_ONE_DAY;
                        endTime = endTime + Constants.TIMESTAMP_ONE_DAY;
                    }
                    if(!(timePointerOneDay < endTimeOneDay && timePointerOneDay >= startTimeOneDay)){
                        continue;
                    }
                    if(!(data.getTimePointer() < endTime && data.getTimePointer() > startTime)){
                        continue;
                    }
                    //通过所有条件，匹配到规则(含有时间段)
                    lastRule = feeRuleParkings.get(i);
                    matchFlag= true;
                    //免费时间
                    if (this.getFreeTime(data, feeRuleParkings.get(i), realEndTime, endTime)) {
                        break;
                    }
                    //首小时费用
                    if (this.getFirstHourFee(data, feeRuleParkings.get(i), realStartTime, realEndTime, endTime)) {
                        break;
                    }
                    //计算次小时费用
                    if (this.getOtherTimeFee(data, feeRuleParkings.get(i), endTime, realEndTime)) {
                        break;
                    }
                }
                if(i == (feeRuleParkingSize-1) && !matchFlag){
                    //如果没有匹配到规则，直接退出while循环
                    break;
                }
            }
        }
        //根据最后一条规则计算超时费用和封顶费用
        if (null != lastRule) {
            //计算超时费用
            BigDecimal overTimeFeeUnit = lastRule.getOverTimeFee();
            //如果规则存在超时费用
            if (null != overTimeFeeUnit && overTimeFeeUnit.compareTo(Constants.BIGDECIMAL_ZERO) == 1) {
                Reservation reservation = reservationRepository.findOne(parkingOrder.getReservationId());
                //预约结束时间
                Long reserveEndTime = reservation.getEndTime();
                //最小计费周期
                String minChargingPeriod = parklotParamsMapper.queryParklotParamsValue(parkingOrder.getParklotId(), Constants.MIN_CHARGING_PERIOD);
                if (StringUtils.isEmpty(minChargingPeriod)) {
                    minChargingPeriod = Constants.MIN_CHARGING_PERIOD_DEFAULT;
                }
                overTime = realEndTime - reserveEndTime;
                if (overTime > 0) {
                    parkingOrder.setOvertime(overTime);
                    BigDecimal overTimePeriod = new BigDecimal(TimeUtil.getPeriod(overTime, Integer.valueOf(minChargingPeriod)));
                    overTimeFee = overTimeFeeUnit.multiply(overTimePeriod);
                }
            }
            BigDecimal maxFee = lastRule.getMaxFee();
            if(null != maxFee && maxFee.compareTo(Constants.BIGDECIMAL_ZERO) > 0) {
                data.setParkingFee((data.getParkingFee().compareTo(maxFee) >= 0) ? maxFee : data.getParkingFee());
            }
        }
        parkingOrder.setTotalFee(data.getParkingFee().setScale(2, BigDecimal.ROUND_HALF_UP));
        parkingOrder.setOvertimeFee(overTimeFee.setScale(2, BigDecimal.ROUND_HALF_UP));
        return data.getParkingFee().setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 计算免费时常
     *
     * @param data
     * @param feeRuleParking
     * @param realEndTime
     * @param endTime
     * @return
     */
    private Boolean getFreeTime(ParkingRuleData data, FeeRuleParking feeRuleParking, Long realEndTime, Long endTime) {
        Integer freeUseTime;
        //如果免费时间为空,为第一次匹配到规则,需要计入免费时间
        if (null == data.getFreeUseTime()) {
            freeUseTime = feeRuleParking.getFreeUseTime();
            freeUseTime = (null == freeUseTime) ? 0 : freeUseTime;
            data.setFreeUseTime(freeUseTime);
            Long freeUseTimeLong = TimeUtil.minutesToMilliSeconds(freeUseTime);
            Long freeUseTimeEnd = freeUseTimeLong + data.getTimePointer();
            //免费结束时间超过了离场时间，整个费用计算没有产生费用
            if (freeUseTimeEnd >= realEndTime) {
                //时间指针指向离场时间
                data.setTimePointer(realEndTime);
                return true;
            }
            //如果有结束时间(基础规则没有时间段)
            if (null != endTime) {
                Long endTimeSameDayWithFreeUseTimeEnd = TimeUtil.changeTime3day(endTime, freeUseTimeEnd);
                //如果免费结束时间超过了时间段的结束时间，本次规则计算没有产生费用
                if (freeUseTimeEnd > endTimeSameDayWithFreeUseTimeEnd) {
                    //时间指针指向免费结束时间
                    data.setTimePointer(freeUseTimeEnd);
                    return true;
                }
            } else {//反之没有结束时间
                data.setTimePointer(freeUseTimeEnd);
            }

        }
        return false;
    }


    /**
     * 首小时费用
     *
     * @param data
     * @param feeRuleParking
     * @param realStartTime
     * @param realEndTime
     * @param endTime
     * @return
     */
    private Boolean getFirstHourFee(ParkingRuleData data, FeeRuleParking feeRuleParking, Long realStartTime, Long realEndTime, Long endTime) {
        BigDecimal firstHourFee;
        //如果首小时费用为空，需要计算首小时费用
        if (null == data.getFirstHourFee()) {
            firstHourFee = (null == feeRuleParking.getFirstHourFee()) ? Constants.BIGDECIMAL_ZERO : feeRuleParking.getFirstHourFee();
            data.setFirstHourFee(firstHourFee);
            //如果存在首小时费用，计算首小时费用
            if (firstHourFee.compareTo(Constants.BIGDECIMAL_ZERO) == 1) {
                Long firstHourTimeEnd;
                //如果免费时间大于等于一个小时，首小时收费从免费时间结束之后开始计算
                if (null != data.getFreeUseTime() && data.getFreeUseTime() >= 60) {
                    firstHourTimeEnd = data.getTimePointer() + TimeUtil.minutesToMilliSeconds(60);
                } else {//反之，首小时收费从停车开始时间之后开始计算
                    firstHourTimeEnd = realStartTime + TimeUtil.minutesToMilliSeconds(60);
                }
                //首小时收费结束时间超过了离场时间
                if (firstHourTimeEnd >= realEndTime) {
                    //时间指针指向离场时间
                    data.setTimePointer(realEndTime);
                    data.setFirstHourFee(firstHourFee);
                    data.setParkingFee(firstHourFee);
                    return true;
                }
                //时间指针指向首小时收费结束时间
                data.setTimePointer(firstHourTimeEnd);
                data.setFirstHourFee(firstHourFee);
                data.setParkingFee(firstHourFee);
                //如果有结束时间(基础规则没有时间段)
                if (null != endTime) {
                    Long endTimeSameDayWithFirstHourTimeEnd = TimeUtil.changeTime3day(endTime, firstHourTimeEnd);
                    //首小时收费结束时间超过了时间段结束时间
                    if (firstHourTimeEnd >= endTimeSameDayWithFirstHourTimeEnd) {
                        data.setParkingFee(firstHourFee);
                        return true;
                    }
                }

            }
        }
        return false;
    }

    /**
     * 计算次小时费用
     *
     * @param data
     * @param feeRuleParking
     * @param endTime
     * @param realEndTime
     * @return
     */
    private Boolean getOtherTimeFee(ParkingRuleData data, FeeRuleParking feeRuleParking, Long endTime, Long realEndTime) {
        BigDecimal otherTimeFee = feeRuleParking.getOtherTimeFee();
        Long differenceTime;
        if (null != endTime) {
            Long endTimeSameDayWithTimePointer = TimeUtil.changeTime3day(endTime, data.getTimePointer());
            //如果实际离开时间小于时间段结束时间
            if (realEndTime <= endTimeSameDayWithTimePointer) {
                differenceTime = realEndTime - data.getTimePointer();
            } else {
                differenceTime = endTimeSameDayWithTimePointer - data.getTimePointer();
            }
        } else {
            differenceTime = realEndTime - data.getTimePointer();
        }
        BigDecimal otherTimeFeePeriod = new BigDecimal(TimeUtil.getPeriod(differenceTime, feeRuleParking.getOtherTimePeriod()));
        data.setParkingFee(data.getParkingFee().add(otherTimeFee.multiply(otherTimeFeePeriod)));
        //时间指针指向收过费用的时间
        data.setTimePointer(data.getTimePointer() + otherTimeFeePeriod.intValue() * TimeUtil.minutesToMilliSeconds(Integer.valueOf(feeRuleParking.getOtherTimePeriod())));
        return true;
    }


    /**
     * 查询下个规则时间段的开始时间
     *
     * @param realEndTime
     * @param timePointer
     * @param infoData
     * @return
     */
    private Long findNextFeeRuleStartTime(Long realEndTime, Long timePointer, ParkingFeeRuleInfoData infoData) {
        Integer type = infoData.getType();
        List<FeeRuleParking> feeRuleParkings = infoData.getFeeRuleParkings();
        for (FeeRuleParking feeRuleParking : feeRuleParkings) {
            Long startTime = feeRuleParking.getStartTime();
            Long endTime = feeRuleParking.getEndTime();
            Integer weekDay = feeRuleParking.getWeekday();
            if (null != startTime) {
                //遍历改规则所有时间段
                int day = TimeUtil.differentDaysByMillisecond(startTime, endTime);
                for (int i = 0; i <= day; i++) {
                    Long startTimeTemp = startTime + i * Constants.TIMESTAMP_ONE_DAY;
                    Long endTimeTemp = endTime - (day - i) * Constants.TIMESTAMP_ONE_DAY;
                    //如果规则分为工作日/周末
                    if (1 == type) {
                        int dayOfWeek = TimeUtil.isWeekDay(startTimeTemp) ? 1 : 0;
                        if (weekDay != dayOfWeek) {
                            continue;
                        }
                    }
                    //如果和停车时间相交，返回下个规则时间段的开始时间
                    if (!(timePointer > endTimeTemp || realEndTime < startTimeTemp)) {
                        return startTimeTemp;
                    }
                }
            }
        }
        return null;
    }

    private boolean checkIfRepeat(Long realStartTime, List<OrderParking> parkingOrders) {
        OrderParking lastOrderParking;
        if (null != parkingOrders && Constants.EMPTY_CAPACITY < parkingOrders.size()) {
            lastOrderParking = parkingOrders.get(Constants.FIRST_INDEX);
            log.info("last paid parkingOrder: {}", lastOrderParking.getRealStartTime());
            return lastOrderParking.getRealStartTime().equals(realStartTime);
        }
        return false;
    }

    /**
     * 释放车位
     *
     * @param parkingOrder 订单
     */
    private void releaseParkloc(OrderParking parkingOrder) {
        log.info("release parkloc start");
        Parkloc parkloc = parklocRepository.findOne(parkingOrder.getParklocId());
        Long num = shareMapper.countParklocReservable();
        log.info("current available reserve time has {}", num);
        if (null != num && num > Constants.EMPTY_CAPACITY) {
            parkloc.setState(Status.Parkloc.PUBLISHED.getInt());
        } else {
            parkloc.setState(Status.Parkloc.UNPUBLISHED.getInt());
        }
        parklocRepository.save(parkloc);
    }


    public void dealWithToBePublish(OrderParking orderParking) {
        log.info("开始执行处理待修改待取消线程");
        if (null == orderParking) {
            log.info("无效的orderparking对象");
            return;
        }
        Integer reservationId = orderParking.getReservationId();
        Integer parklocId = orderParking.getParklocId();
        Publish publish = publishRepository.findByReservationId(reservationId);
        if (null == publish) {
            log.info("无效的publish对象");
            return;
        }
        Integer publishId = publish.getId();
        Integer publishState = publish.getState();
        log.info("publishId is {},publishState is {}", publishId, publishState);
        //如果是待取消
        if (Status.Publish.TOBECANCELLED.getInt().equals(publishState)) {
            log.info("进入待取消");
            //发布状态改为失效
            publishRepository.updateState(publishId, Status.Common.INVALID.getInt());
            shareRepository.updateState(publishId, Status.Common.INVALID.getInt(), Status.Common.VALID.getInt());
            //如果车位没有发布的车位，更新车位状态
            List<Integer> states = new ArrayList<>();
            states.add(Status.Publish.TOBEALTER.getInt());
            states.add(Status.Publish.TOBECANCELLED.getInt());
            states.add(Status.Common.VALID.getInt());
            List<Publish> publishList = publishRepository.findByParklocIdAndStates(parklocId, states);
            if (publishList.size() == Constants.MIN_NON_NEGATIVE_INTEGER) {
                parklocRepository.updateState(parklocId, Status.Parkloc.UNPUBLISHED.getInt());
            }
        }
        //如果是待修改
        else if (Status.Publish.TOBEALTER.getInt().equals(publishState)) {
            log.info("进入待修改");
            //共享状态改为失效
            shareRepository.updateState(publishId, Status.Common.INVALID.getInt(), Status.Common.VALID.getInt());
            //把发布状态改为有效
            publishRepository.updateState(publishId, Status.Common.VALID.getInt());
            //重新生成共享时间
            int todayOfWeek = TimeUtil.getDayOfWeekToday();
            Calendar calendar = Calendar.getInstance();
            Long startTime;
            Long endTime;
            // 发布时间是否跨天
            if (TimeUtil.isSameDay(calendar, publish.getStartTime(), publish.getEndTime())) {
                endTime = TimeUtil.changeTime2day(publish.getEndTime(), 0);
            } else {
                endTime = TimeUtil.changeTime2day(publish.getEndTime(), 1);
            }
            // 把时间转换成当天的日期
            startTime = TimeUtil.changeTime2day(publish.getStartTime(), 0);
            this.share(publish.getMode(), parklocId, publishId, startTime, endTime, publish.getDayOfWeek(), todayOfWeek);
        }

        // 更新该车场的parklot_amount表的数据
        Integer parklotId = orderParking.getParklotId();
        parklotAmountService.updateParklotAmountInfoByParklotId(parklotId, "dealWithToBePublish 停车离场");
    }


    private void share(Integer mode, Integer parklocId, Integer publishId, Long startTime, Long endTime, String dayOfWeek, int todayOfWeek) {
        //如果不是重复发布直接插入到Share表
        Share share;
        if (Constants.SINGLE_MODE.equals(mode)) {
            share = new Share(parklocId, publishId, startTime, endTime, Status.Common.VALID.getInt());
            if (null == shareRepository.save(share)) {
                log.error("parklocId:" + parklocId + " result:Share " + Status.ApiErr.INSERT_ERROR.getMsg());
                throw new QhieException(Status.ApiErr.INSERT_ERROR);
            }
        } else {
            //如果重复发布中包含了今天插入到Share表
            if (dayOfWeek.contains(String.valueOf(todayOfWeek))) {
                share = new Share(parklocId, publishId, startTime, endTime, Status.Common.VALID.getInt());
                if (null == shareRepository.save(share)) {
                    log.error("parklocId:" + parklocId + " result:Share " + Status.ApiErr.INSERT_ERROR.getMsg());
                    throw new QhieException(Status.ApiErr.INSERT_ERROR);
                }
            }
        }
    }

    /**
     * 更新车位状态
     *
     * @param totalFee     总金额
     * @param realEndTime  实际结束时间
     * @param parkingOrder 订单
     * @return 订单
     */
    private OrderParking updateParkingState(BigDecimal totalFee, BigDecimal realFee, Long realEndTime, OrderParking parkingOrder, Integer allocable) {
        Long now = System.currentTimeMillis();
        Integer parklocId = parkingOrder.getParklocId();
        parkingOrder.setRealEndTime(realEndTime);
        parkingOrder.setPayTime(realEndTime);
        parkingOrder.setTotalFee(totalFee.divide(Constants.BIGDECIMAL_ONE_HUNDRED, QhieCurrency.DEFAULT_SCALE, RoundingMode.HALF_UP));
        parkingOrder.setRealFee(realFee.divide(Constants.BIGDECIMAL_ONE_HUNDRED, QhieCurrency.DEFAULT_SCALE, RoundingMode.HALF_UP));
        parkingOrder.setPayTime(now);
        Reservation reservation = reservationRepository.findOne(parkingOrder.getReservationId());
        if (null != realEndTime && realEndTime > reservation.getEndTime()) {
            parkingOrder.setOvertime(realEndTime - reservation.getEndTime());
        }
        parkingOrder.setPayChannel(Constants.PAY_CHANNEL_CASH);
        if (Constants.PARKING_FEE_ALLOCABLE.equals(allocable) && Constants.BIGDECIMAL_ZERO.compareTo(realFee) < 0) {
            //计算分成金额
            BigDecimal ownerIncome = Constants.BIGDECIMAL_ZERO;
            BigDecimal manageIncome = Constants.BIGDECIMAL_ZERO;
            BigDecimal platformIncome = Constants.BIGDECIMAL_ZERO;
            //判断业主是否是管理员
            UserMobile userMobile = userMobileRepository.findByParklocId(parklocId);
            if (null == userMobile) {
                throw new QhieException(Status.ApiErr.NONEXISTENT_USER);
            }
            if (Constants.PARKING_ADMIN == userMobile.getType()) {
                String managePercentage = parklotParamsBRepository.findValueByParklocId(parklocId, Constants.PROPCOMP_APPOINTMENT_PERCENTAGE, Status.Common.VALID.getInt());
                if (StringUtils.isEmpty(managePercentage)) {
                    managePercentage = Constants.PROPCOMP_APPOINTMENT_PERCENTAGE_DEFAULT;
                }
                manageIncome = NumberUtils.getFeeCeil(totalFee.divide(Constants.BIGDECIMAL_ONE_HUNDRED, QhieCurrency.DEFAULT_SCALE, RoundingMode.HALF_UP), Integer.valueOf(managePercentage));
                platformIncome = NumberUtils.getPlatformFeeCeil(totalFee.divide(Constants.BIGDECIMAL_ONE_HUNDRED, QhieCurrency.DEFAULT_SCALE, RoundingMode.HALF_UP), ownerIncome, manageIncome);
            } else {
                String ownerPercentage = parklotParamsBRepository.findValueByParklocId(parklocId, Constants.OWNER_PERCENTAGE, Status.Common.VALID.getInt());
                if (StringUtils.isEmpty(ownerPercentage)) {
                    ownerPercentage = Constants.OWNER_PERCENTAGE_DEFAULT;
                }
                String propcomPpercentage = parklotParamsBRepository.findValueByParklocId(parklocId, Constants.PROPCOMP_PERCENTAGE, Status.Common.VALID.getInt());
                if (StringUtils.isEmpty(propcomPpercentage)) {
                    propcomPpercentage = Constants.PROPCOMP_PERCENTAGE_DEFAULT;
                }
                ownerIncome = NumberUtils.getFeeCeil(totalFee.divide(Constants.BIGDECIMAL_ONE_HUNDRED, QhieCurrency.DEFAULT_SCALE, RoundingMode.HALF_UP), Integer.valueOf(ownerPercentage));
                manageIncome = NumberUtils.getFeeCeil(totalFee.divide(Constants.BIGDECIMAL_ONE_HUNDRED, QhieCurrency.DEFAULT_SCALE, RoundingMode.HALF_UP), Integer.valueOf(propcomPpercentage));
                platformIncome = NumberUtils.getPlatformFeeCeil(totalFee.divide(Constants.BIGDECIMAL_ONE_HUNDRED, QhieCurrency.DEFAULT_SCALE, RoundingMode.HALF_UP), ownerIncome, manageIncome);
            }
            parkingOrder.setOwnerIncome(ownerIncome);
            parkingOrder.setManageIncome(manageIncome);
            parkingOrder.setPlatformIncome(platformIncome);
            //更新车位所有者分成(停车分成)
            balanceUserRepository.updateBalanceEarnByParklocId(parkingOrder.getParklocId(), ownerIncome);
            //更新停车场分成（停车分成）
            balanceParklotRepository.updateBalanceByParklotId(parkingOrder.getParklotId(), manageIncome);
        }

        parkingOrder.setState(Status.OrderParking.PAID.getInt());
        parkingOrder.setParkingFeePaidFlag(1);
        OrderParking orderParkingResp = orderParkingRepository.save(parkingOrder);
        if (null == orderParkingResp) {
            throw new QhieException(Status.ApiErr.INSERT_ERROR);
        }
        return orderParkingResp;
    }

    @Override
    public Resp getFee(){
        OrderParking orderParking = orderParkingRepository.getOne(44135);
        BigDecimal totalFee  = this.calculateParkingTotalFee(orderParking.getRealEndTime(),orderParking);
        return RespUtil.successResp(totalFee);
    }
}

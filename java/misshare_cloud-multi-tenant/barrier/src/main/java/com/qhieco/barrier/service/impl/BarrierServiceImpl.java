package com.qhieco.barrier.service.impl;

import com.google.gson.Gson;
import com.qhieco.barrier.boostedgoal.request.BoostedGoalParkingRequest;
import com.qhieco.barrier.exception.QhieException;
import com.qhieco.barrier.keytop.request.KeyTopCancelRequest;
import com.qhieco.barrier.service.BarrierService;
import com.qhieco.barrier.service.ParklotAmountService;
import com.qhieco.common.QhieCurrency;
import com.qhieco.commonentity.*;
import com.qhieco.commonentity.iotdevice.Lock;
import com.qhieco.commonrepo.*;
import com.qhieco.commonrepo.iot.BalanceParklotRepository;
import com.qhieco.commonrepo.iot.LockRepository;
import com.qhieco.config.ConfigurationFiles;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.mapper.FeeRuleParkingMapper;
import com.qhieco.mapper.ParklotParamsMapper;
import com.qhieco.mapper.ShareMapper;
import com.qhieco.push.QhMessageTemplate;
import com.qhieco.push.QhMessageType;
import com.qhieco.request.api.BarrierInfoRequest;
import com.qhieco.request.api.ParkingRuleData;
import com.qhieco.request.api.PostCarInfoRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.ParkingFeeRuleInfoData;
import com.qhieco.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/4/22 下午10:15
 * <p>
 * 类说明：
 *     道闸服务的实现类
 */
@Service
@Slf4j
public class BarrierServiceImpl implements BarrierService {

    @Autowired
    private ParklotRepository parklotRepository;

    @Autowired
    private PlateRepository plateRepository;

    @Autowired
    private OrderParkingRepository orderParkingRepository;

    @Autowired
    private UserExtraInfoRepository userExtraInfoRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ParklocRepository parklocRepository;

    @Autowired
    private ShareMapper shareMapper;

    @Autowired
    private BalanceUserRepository balanceUserRepository;

    @Autowired
    private BalanceParklotRepository balanceParklotRepository;

    @Autowired
    PublishRepository publishRepository;

    @Autowired
    ShareRepository shareRepository;

    @Autowired
    private ParklotAmountService parklotAmountService;

    @Autowired
    private ParklotParamsMapper parklotParamsMapper;

    @Autowired
    ParklotParamsBRepository parklotParamsBRepository;

    @Autowired
    OrderTotalRepository orderTotalRepository;

    @Autowired
    IntegralRepository integralRepository;

    @Autowired
    UserMobileRepository userMobileRepository;
    @Autowired
    private ConfigurationFiles configuration;
    @Autowired
    private LockRepository lockRepository;

    @Autowired
    private FeeRuleParkingMapper feeRuleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp process(BarrierInfoRequest barrierInfoRequest) {
        log.info("barrierInfoRequest:{}",barrierInfoRequest.toString());
        String tag = barrierInfoRequest.getTag();
        log.info("tag is {}", tag);
        if (Constants.TAG_ENTER.equals(tag)) {
            log.info("enter");
            addParkingOrder(barrierInfoRequest);
        } else if (Constants.TAG_LEAVE.equals(tag)) {
            log.info("leave");
            updateParkingOrder(barrierInfoRequest);
        } else {
            throw new QhieException(Status.ApiErr.NONEXISTENT_TAG_EPATIC_WEBHOOK);
        }
        return RespUtil.successResp();
    }

    /**
     * 新增停车订单
     * @param barrierInfoRequest 出入场request
     */
    private void addParkingOrder(BarrierInfoRequest barrierInfoRequest) {
        String uniqueId = barrierInfoRequest.getUnique_id();
        if(StringUtils.isEmpty(uniqueId)){
            log.info("uniqueId为空");
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        List<OrderParking> orderParkings = orderParkingRepository.findOrderParkingByUniqueId(uniqueId);
        if(null != orderParkings && orderParkings.size() > 0){
            log.info("重复入场");
            throw new QhieException(Status.ApiErr.REPEAT_PARKING);
        }
        String parkId = barrierInfoRequest.getParklot_id();
        Parklot parklot = parklotRepository.findByExtraParklotId(parkId);
        if(null == parklot){
            throw new QhieException(Status.ApiErr.NONEXISTENT_PARKLOT);
        }
        Integer parklotId = parklot.getId();
        String plateNum = barrierInfoRequest.getLicense();
        log.info("plateNum is {}", plateNum);
        Integer plateId = plateRepository.findByNumber(plateNum, Status.Common.VALID.getInt());
        log.info("plateId is {}, parklot id is {}", plateId, parklotId);
        List<Integer> states = new ArrayList<>();
        states.add(Status.OrderParking.UNPAID.getInt());
        states.add(Status.OrderParking.USED.getInt());
        List<OrderParking> parkingOrders = orderParkingRepository.findByParklotIdAndPlateIdAndStateIn(parklotId, plateId, states);
        log.info("park order is {}", parkingOrders);
        if (null != parkingOrders && parkingOrders.size() > Constants.EMPTY_CAPACITY) {
            for(OrderParking parkingOrder : parkingOrders){
                if(parkingOrder.getState().equals(Status.OrderParking.USED)){
                    log.info("改变订单id:{}的uniqueId为:{},旧的uniqueId为{}",parkingOrder.getId(),uniqueId,parkingOrder.getUniqueId());
                    parkingOrder.setUniqueId(uniqueId);
                    orderParkingRepository.save(parkingOrder);
                }
            }
            return;
        }
        log.info("finding reserve order");
        List<OrderParking> reserveOrders = orderParkingRepository.findByParklotIdAndPlateIdAndStateOrderByIdDesc(parklotId, plateId, Status.OrderParking.RESERVED.getInt());
        if (null == reserveOrders || Constants.EMPTY_CAPACITY == reserveOrders.size()) {
            throw new QhieException(Status.ApiErr.NONEXISTENT_ORDER_RESERVE_PARKING);
        }
        log.info("find the latest reserve order");
        OrderParking reserveOrder = reserveOrders.get(Constants.FIRST_INDEX);
        parkingOrders = orderParkingRepository.findByParklotIdAndPlateIdAndStateOrderByIdDesc(parklotId, plateId, Status.OrderParking.PAID.getInt());
        log.info("find payed order");
        if (null != parkingOrders && parkingOrders.size() > Constants.EMPTY_CAPACITY) {
            OrderParking orderParking = parkingOrders.get(Constants.FIRST_INDEX);
            if (orderParking.getId() > reserveOrder.getId()) {
                throw new QhieException(Status.ApiErr.REPEAT_PARKING);
            }
        }
        log.info("reserve order is {}",reserveOrder);
        /*Long enterTime = TimeUtil.strToTimestamp(barrierInfoRequest.getEnter_time());
        if(null == enterTime){
            throw new QhieException(Status.ApiErr.TIME_ILLEGAL);
        }*/
        Long now = System.currentTimeMillis();
        BigDecimal initialMoney = new BigDecimal(0.00);
        OrderParking orderParking = new OrderParking();
        orderParking.setReserveId(reserveOrder.getId());
        orderParking.setSerialNumber(OrderNoGenerator.getOrderNo(Status.OrderType.TYPE_PARKING.getInt(), String.valueOf(reserveOrder.getMobileUserId())));
        orderParking.setMobileUserId(reserveOrder.getMobileUserId());
        orderParking.setReservationId(reserveOrder.getReservationId());
        orderParking.setParklocId(reserveOrder.getParklocId());
        orderParking.setParklotId(reserveOrder.getParklotId());
        orderParking.setPlateId(reserveOrder.getPlateId());
        orderParking.setRealStartTime(now);
        orderParking.setTotalFee(initialMoney);
        orderParking.setDiscountFee(initialMoney);
        orderParking.setRealFee(initialMoney);
        orderParking.setTripartiteFee(initialMoney);
        orderParking.setPlatformIncome(initialMoney);
        orderParking.setOwnerIncome(initialMoney);
        orderParking.setManageIncome(initialMoney);
        orderParking.setCreateTime(now);
        orderParking.setState(Status.OrderParking.USED.getInt());
        orderParking.setOvertimeFee(Constants.BIGDECIMAL_ZERO);
        orderParking.setUniqueId(uniqueId);
        log.info("save the reserve order");
        OrderParking orderParkingResp = orderParkingRepository.save(orderParking);
        if (null == orderParkingResp) {
            throw new QhieException(Status.ApiErr.INSERT_ERROR);
        }
        //保存到订单总表
        OrderTotal orderTotal = new OrderTotal(reserveOrder.getMobileUserId(),orderParking.getSerialNumber(),Constants.PARKING_ORDER,orderParking.getCreateTime(),Constants.BIGDECIMAL_ZERO,Status.OrderParking.USED.getInt());
        orderTotalRepository.save(orderTotal);
        UserExtraInfo userExtraInfo = userExtraInfoRepository.findByMobileUserId(reserveOrder.getMobileUserId());
        if(null == userExtraInfo || StringUtils.isEmpty(userExtraInfo.getJpushRegId())){
            return;
        }
        log.info("push to user id: " + reserveOrder.getMobileUserId());
        List<Lock> locks=lockRepository.findByParklocId(reserveOrder.getParklocId());
        String btName="";
        String btPassword="";
        String btNumber="";
        Parkloc parkloc=parklocRepository.findOne(reserveOrder.getParklocId());
        if(locks!=null && locks.size()>Constants.EMPTY_CAPACITY){
            btName=locks.get(0).getBtName();
            btPassword=locks.get(0).getBtPassword();
            btNumber=parkloc.getNumber();
        }
        //给前端发送自定义消息提示
        QhPushUtil.getInstance().sendQhPush(userExtraInfo.getJpushRegId(), QhMessageType.CUSTOM, QhMessageTemplate.ENTER,
                orderParkingResp.getId().toString(),btName,btPassword,btNumber);
    }

    /**
     * 更新停车订单
     * @param barrierInfoRequest 出入场request
     */
    private void updateParkingOrder(BarrierInfoRequest barrierInfoRequest) {
        String parkId = barrierInfoRequest.getParklot_id();
        String uniqueId = barrierInfoRequest.getUnique_id();
        Parklot parklot = parklotRepository.findByExtraParklotId(parkId);
        Integer parklotId = parklot.getId();
        Integer allocable = parklot.getAllocable();
        String plateNum = barrierInfoRequest.getLicense();
        Long realEndTime = System.currentTimeMillis();
        if(null == realEndTime){
            throw new QhieException(Status.ApiErr.TIME_ILLEGAL);
        }
        BigDecimal totalFee = barrierInfoRequest.getTotal_amount();
        BigDecimal unpaidAmount = barrierInfoRequest.getUnpaid_amount();
        if(null == unpaidAmount){
            unpaidAmount = Constants.BIGDECIMAL_ZERO;
        }
        BigDecimal cashPaidAmount = barrierInfoRequest.getCash_paid_amount();
        if(null == cashPaidAmount){
            cashPaidAmount = Constants.BIGDECIMAL_ZERO;
        }
        BigDecimal ePaidAmount = barrierInfoRequest.getE_paid_amount();
        if(null == ePaidAmount){
            ePaidAmount = Constants.BIGDECIMAL_ZERO;
        }
        BigDecimal realFee = cashPaidAmount.add(ePaidAmount);
        log.info("总金额（分）:{}",totalFee);
        log.info("未付金额（分）:{}",unpaidAmount);
        log.info("实际金额（分）:{}",realFee);

        List<OrderParking> parkingOrders = orderParkingRepository.findOrderParkingByUniqueId(uniqueId);
        if(null == parkingOrders || parkingOrders.size() == 0){
            log.info("根据uniqueId未找到停车订单");
            throw new QhieException(Status.ApiErr.NONEXISTENT_PARKING_ORDER);
        }
        if(!parkingOrders.get(Constants.FIRST_INDEX).getState().equals(Status.OrderParking.USED.getInt())){
            log.info("重复出场");
            throw new QhieException(Status.ApiErr.REPEAT_LEAVE);
        }
        OrderParking parkingOrder = parkingOrders.get(Constants.FIRST_INDEX);
        Integer plateId = plateRepository.findByNumber(plateNum, Status.Common.VALID.getInt());
        if(!parkingOrder.getPlateId().equals(plateId)){
            log.info("车牌不匹配");
            throw new QhieException(Status.ApiErr.MISMATCH_PLATE);
        }
        log.info("last used parkingOrder: {}", parkingOrder);
        List<Integral> integrals = integralRepository.findByIntegralCodeAndState(Constants.INTEGRAL_TIME_OUT,Status.Common.VALID.getInt());
        Integral integralTimeOutAdd = null;
        Integral integralTimeOutSubtract = null;
        if(null != integrals && integrals.size() > 0){
            for(Integral integral : integrals){
                if(integral.getIntegralType() == Constants.INTEGRAL_ADD){
                    integralTimeOutAdd= integral;
                }else{
                    integralTimeOutSubtract= integral;
                }
            }
        }
        UserMobile userMobile = userMobileRepository.findOne(parkingOrder.getMobileUserId());
        Reservation reservation = reservationRepository.findOne(parkingOrder.getReservationId());
        Long overtime = null;
        BigDecimal overTimeFee = null;
        //如果超时,计算超时金额
        if (null != realEndTime && realEndTime > reservation.getEndTime()) {
            overtime = realEndTime - reservation.getEndTime();
            String minChargingPeriod = parklotParamsMapper.queryParklotParamsValue(parkingOrder.getParklotId(), Constants.MIN_CHARGING_PERIOD);
            if (StringUtils.isEmpty(minChargingPeriod)) {
                minChargingPeriod = Constants.MIN_CHARGING_PERIOD_DEFAULT;
            }
            BigDecimal overTimePeriod = new BigDecimal(TimeUtil.getPeriod(overtime, Integer.valueOf(minChargingPeriod)));
            ParkingFeeRuleInfoData infoData = feeRuleMapper.queryParkingFeeRuleById(parklotId);
            List<FeeRuleParking> feeRuleParkings = infoData.getFeeRuleParkings();
            if (feeRuleParkings == null) {
                log.error("车场收费规则为空，终止计算");
                throw new QhieException(Status.ApiErr.UNKNOWN_ERROR);
            }
            Integer type = infoData.getType();
            Integer dayOfWeek = 2;
            BigDecimal overTimeFeeUnit = Constants.BIGDECIMAL_ZERO;
            //如果规则分为工作日/周末
            if(1 == type){
                dayOfWeek = TimeUtil.isWeekDay(realEndTime) ? 1 : 0;
            }
            if(null != feeRuleParkings){
                for(FeeRuleParking feeRuleParking : feeRuleParkings){
                    Integer weekEnd = feeRuleParking.getWeekday();
                    Long startTime = feeRuleParking.getStartTime();
                    Long endTime = feeRuleParking.getEndTime();
                    if(!dayOfWeek.equals(weekEnd)){
                        continue;
                    }
                    //如果startTime为NULL，为24小时的计费规则，该计费规则按照排序排在最后  说明其他时间段都不符合，使用该规则
                    if(null == startTime){
                        overTimeFeeUnit = feeRuleParking.getOverTimeFee();
                        break;
                    }
                    //把三个时间放到同一天,跨天就加一天
                    Long nowOneDay = TimeUtil.formatOneDayTime(realEndTime);
                    Long startTimeOneDay = TimeUtil.formatOneDayTime(startTime);
                    Long endTimeOneDay = TimeUtil.formatOneDayTime(endTime);
                    if(startTimeOneDay >= endTimeOneDay){
                        endTimeOneDay = endTimeOneDay + Constants.TIMESTAMP_ONE_DAY;
                        endTime = endTime + Constants.TIMESTAMP_ONE_DAY;
                    }
                    //判断时间段是否符合
                    if(!(nowOneDay < endTimeOneDay && nowOneDay >= startTimeOneDay)){
                        continue;
                    }
                    //判断时间区间是否符合
                    if(!(realEndTime < endTime && realEndTime > startTime)){
                        continue;
                    }
                    overTimeFeeUnit = feeRuleParking.getOverTimeFee();
                    break;
                }
            }
            overTimeFee = overTimeFeeUnit.multiply(overTimePeriod);
        }
        //没有未付金额
        if(unpaidAmount.compareTo(Constants.BIGDECIMAL_ZERO) == 0){
            if(null != overTimeFee && overTimeFee.compareTo(Constants.BIGDECIMAL_ZERO) >= 0) {
                BigDecimal orderTotalFee = totalFee;
                parkingOrder.setOvertime(overtime);
                parkingOrder.setOvertimeFee(overTimeFee);
                parkingOrder.setTotalFee(orderTotalFee.divide(Constants.BIGDECIMAL_ONE_HUNDRED, QhieCurrency.DEFAULT_SCALE, RoundingMode.HALF_UP));
                parkingOrder.setState(Status.OrderParking.UNPAID.getInt());
                parkingOrder.setRealEndTime(realEndTime);
                parkingOrder.setParkingFeePaidFlag(1);
                orderParkingRepository.save(parkingOrder);
            }else{
                OrderParking orderParkingResp = updateParkingState(totalFee,realFee, realEndTime, parkingOrder,allocable);
                //更新预约订单的可开票状态
                Integer reserveOrderId = parkingOrder.getReserveId();
                orderParkingRepository.updateInvoiceState(reserveOrderId,Status.InvoiceStatus.UNMAKE.getInt());
                //更新用户的可开票金额（预约记录开票）
                OrderParking reserveOrder = orderParkingRepository.findOne(reserveOrderId);
                balanceUserRepository.updateBalanceInvoice(reserveOrder.getRealFee(),parkingOrder.getMobileUserId());
                //更新车位所有者分成(预约分成)
                balanceUserRepository.updateBalanceEarnByParklocId(reserveOrder.getParklocId(),reserveOrder.getOwnerIncome());
                //更新停车场分成（预约分成）
                balanceParklotRepository.updateBalanceByParklotId(reserveOrder.getParklotId(),reserveOrder.getManageIncome());
            }
        }else{//存在未支付金额
            BigDecimal orderTotalFee = unpaidAmount;
            //如果超时
            if (null != overtime) {
                parkingOrder.setOvertime(overtime);
                parkingOrder.setOvertimeFee(overTimeFee);
            }
            parkingOrder.setTotalFee(orderTotalFee.divide(Constants.BIGDECIMAL_ONE_HUNDRED, QhieCurrency.DEFAULT_SCALE, RoundingMode.HALF_UP));
            parkingOrder.setState(Status.OrderParking.UNPAID.getInt());
            parkingOrder.setRealEndTime(realEndTime);
            parkingOrder.setParkingFeePaidFlag(0);
            orderParkingRepository.save(parkingOrder);
        }
        //如果超时，扣除积分，未超时添加积分
        if (null != realEndTime && realEndTime > reservation.getEndTime()) {
            if(null != integralTimeOutSubtract){
                if(null != userMobile){
                    Integer userIntegral = userMobile.getIntegral() - integralTimeOutSubtract.getIntegralPluses() < 0  ? 0 : userMobile.getIntegral() - integralTimeOutSubtract.getIntegralPluses();
                    log.info("扣除积分，扣除后剩余积分:{}",userIntegral);
                    userMobileRepository.updateUserMobilePlusIntegral(parkingOrder.getMobileUserId(),userIntegral);
                }
            }
        }else{
            if(null != integralTimeOutAdd){
                if(null != userMobile){
                    Integer userIntegral = userMobile.getIntegral() + integralTimeOutAdd.getIntegralPluses() > 100 ? 100 : userMobile.getIntegral() + integralTimeOutAdd.getIntegralPluses();
                    log.info("增加积分，增加后剩余积分:{}",userIntegral);
                    userMobileRepository.updateUserMobilePlusIntegral(parkingOrder.getMobileUserId(),userIntegral);
                }
            }
        }


        releaseParkloc(parkingOrder);
        //线程处理待取消待发布
        dealWithToBePublish(parkingOrder);
        Integer barrierManufacturer = parklot.getBarrierManufacturer();
        //删除白名单
        if(Constants.BARRIER_MANUFACTURER_BOOSTED_GOAL.equals(barrierManufacturer)){
            this.registeredTemp(parkingOrder);
        }
        /*
         * 推送信息
         */
        UserExtraInfo userExtraInfo = userExtraInfoRepository.findByMobileUserId(parkingOrder.getMobileUserId());
        if(null == userExtraInfo || StringUtils.isEmpty(userExtraInfo.getJpushRegId())){
            return;
        }
        //给前端发送自定义消息提示
        List<Lock> locks=lockRepository.findByParklocId(parkingOrder.getParklocId());
        String btName="";
        String btPassword="";
        String btNumber="";
        Parkloc parkloc=parklocRepository.findOne(parkingOrder.getParklocId());
        if(locks!=null && locks.size()>Constants.EMPTY_CAPACITY){
            btName=locks.get(0).getBtName();
            btPassword=locks.get(0).getBtPassword();
            btNumber=parkloc.getNumber();
        }
        QhPushUtil.getInstance().sendQhPush(userExtraInfo.getJpushRegId(), QhMessageType.CUSTOM, QhMessageTemplate.LEAVE,
                parkingOrder.getId().toString(),btName,btPassword,btNumber);
        //如果订单费用大于0，发送消息给车主
//        if(null != totalFee && totalFee.compareTo(Constants.BIGDECIMAL_ZERO) > 0){
//            Parkloc parkloc = parklocRepository.findOne(parkingOrder.getParklocId());
//            Parklot parklot = parklotRepository.findOne(parkingOrder.getParklotId());
//            String placeHolder = parklot.getName() + parkloc.getNumber();
//            QhPushUtil.getInstance().sendQhPush(userExtraInfo.getJpushRegId(), QhMessageType.WALLET, QhMessageTemplate.LEAVE_PAY_PROMPT,
//                    placeHolder);
//            //保存到message表
//            Integer userId = parkingOrder.getMobileUserId();
//            String content = String.format(QhMessageTemplate.LEAVE_PAY_PROMPT, placeHolder);
//            Message message = new Message(userId,Constants.MIN_NON_NEGATIVE_INTEGER,QhMessageType.WALLET.getTitle(),content,"",Constants.MESSAGE_TYPE_PERSONAL,Constants.MESSAGE_KIND_JPUSH,Status.Common.VALID.getInt(),now);
//            messageRepository.save(message);
//        }
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
     * 更新车位状态
     * @param totalFee 总金额
     * @param realEndTime 实际结束时间
     * @param parkingOrder 订单
     * @return 订单
     */
    private OrderParking updateParkingState(BigDecimal totalFee,BigDecimal realFee, Long realEndTime, OrderParking parkingOrder,Integer allocable) {
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
        if(Constants.PARKING_FEE_ALLOCABLE.equals(allocable) && Constants.BIGDECIMAL_ZERO.compareTo(realFee) < 0){
            //计算分成金额
            BigDecimal ownerIncome = Constants.BIGDECIMAL_ZERO;
            BigDecimal manageIncome = Constants.BIGDECIMAL_ZERO;
            BigDecimal platformIncome = Constants.BIGDECIMAL_ZERO;
            //判断业主是否是管理员
            UserMobile userMobile = userMobileRepository.findByParklocId(parklocId);
            if(null == userMobile){
                throw new com.qhieco.apiservice.exception.QhieException(Status.ApiErr.NONEXISTENT_USER);
            }
            if(Constants.PARKING_ADMIN == userMobile.getType()) {
                String managePercentage = parklotParamsBRepository.findValueByParklocId(parklocId, Constants.PROPCOMP_APPOINTMENT_PERCENTAGE, Status.Common.VALID.getInt());
                if (StringUtils.isEmpty(managePercentage)) {
                    managePercentage = Constants.PROPCOMP_APPOINTMENT_PERCENTAGE_DEFAULT;
                }
                manageIncome = NumberUtils.getFeeCeil(totalFee.divide(Constants.BIGDECIMAL_ONE_HUNDRED, QhieCurrency.DEFAULT_SCALE, RoundingMode.HALF_UP), Integer.valueOf(managePercentage));
                platformIncome = NumberUtils.getPlatformFeeCeil(totalFee.divide(Constants.BIGDECIMAL_ONE_HUNDRED, QhieCurrency.DEFAULT_SCALE, RoundingMode.HALF_UP), ownerIncome, manageIncome);
            }else{
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
                platformIncome =  NumberUtils.getPlatformFeeCeil(totalFee.divide(Constants.BIGDECIMAL_ONE_HUNDRED, QhieCurrency.DEFAULT_SCALE, RoundingMode.HALF_UP), ownerIncome, manageIncome);
            }
            parkingOrder.setOwnerIncome(ownerIncome);
            parkingOrder.setManageIncome(manageIncome);
            parkingOrder.setPlatformIncome(platformIncome);
            //更新车位所有者分成(停车分成)
            balanceUserRepository.updateBalanceEarnByParklocId(parkingOrder.getParklocId(),ownerIncome);
            //更新停车场分成（停车分成）
            balanceParklotRepository.updateBalanceByParklotId(parkingOrder.getParklotId(),manageIncome);
        }

        parkingOrder.setState(Status.OrderParking.PAID.getInt());
        parkingOrder.setParkingFeePaidFlag(1);
        OrderParking orderParkingResp = orderParkingRepository.save(parkingOrder);
        if (null == orderParkingResp) {
            throw new QhieException(Status.ApiErr.INSERT_ERROR);
        }
        return orderParkingResp;
    }

    /**
     * 使用我们系统的停车计费规则 计算费用
     * @param realEndTime 离场时间
     * @param parkingOrder
     * @return
     * liujiangjiang   2018/6/28
     */
    @Override
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
        List<FeeRuleParking> feeRuleParkings = infoData.getFeeRuleParkings();
        if(null != feeRuleParkings){
            while (data.getTimePointer() < realEndTime){
                Integer feeRuleParkingSize = feeRuleParkings.size();
                Integer i = 0;
                Boolean matchFlag = false;
                for(i= 0;i<feeRuleParkingSize; i++){
                    //如果规则分为工作日/周末
                    if(1 == type){
                        dayOfWeek = TimeUtil.isWeekDay(data.getTimePointer()) ? 1 : 0;
                    }else{
                        dayOfWeek = 2;
                    }
                    Integer weekEnd = feeRuleParkings.get(i).getWeekday();
                    Long startTime = feeRuleParkings.get(i).getStartTime();
                    Long endTime = feeRuleParkings.get(i).getEndTime();
                    if(!dayOfWeek.equals(weekEnd)){
                        continue;
                    }
                    //如果startTime为NULL，为24小时的计费规则，该计费规则按照排序排在最后  当其他时间段都不符合，使用该规则(不含时间段)
                    if(null == startTime){
                        matchFlag = true;
                        lastRule = feeRuleParkings.get(i);
                        //找到下个规则时间段的开始时间，作为本规则的结束时间
                        Long nextStartTime = this.findNextFeeRuleStartTime(realEndTime,data.getTimePointer(),infoData);
                        //免费时间
                        if(this.getFreeTime(data,feeRuleParkings.get(i),realEndTime,nextStartTime)){
                            break;
                        }
                        //首小时费用
                        if(this.getFirstHourFee(data,feeRuleParkings.get(i),realStartTime,realEndTime,nextStartTime)){
                            break;
                        }
                        //计算次小时费用
                        if(this.getOtherTimeFee(data,feeRuleParkings.get(i),nextStartTime,realEndTime)){
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
                    matchFlag = true;
                    //免费时间
                    if(this.getFreeTime(data,feeRuleParkings.get(i),realEndTime,endTime)){
                        break;
                    }
                    //首小时费用
                    if(this.getFirstHourFee(data,feeRuleParkings.get(i),realStartTime,realEndTime,endTime)){
                        break;
                    }
                    //计算次小时费用
                    if(this.getOtherTimeFee(data,feeRuleParkings.get(i),endTime,realEndTime)){
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
        if(null != lastRule){
            //计算超时费用
            BigDecimal overTimeFeeUnit = lastRule.getOverTimeFee();
            //如果规则存在超时费用
            if(null != overTimeFeeUnit && overTimeFeeUnit.compareTo(Constants.BIGDECIMAL_ZERO) == 1){
                Reservation reservation = reservationRepository.findOne(parkingOrder.getReservationId());
                //预约结束时间
                Long reserveEndTime = reservation.getEndTime();
                //最小计费周期
                String minChargingPeriod = parklotParamsMapper.queryParklotParamsValue(parkingOrder.getParklotId(), Constants.MIN_CHARGING_PERIOD);
                if (StringUtils.isEmpty(minChargingPeriod)) {
                    minChargingPeriod = Constants.MIN_CHARGING_PERIOD_DEFAULT;
                }
                Long overTime = realEndTime - reserveEndTime;
                if(overTime > 0){
                    BigDecimal overTimePeriod = new BigDecimal(TimeUtil.getPeriod(overTime, Integer.valueOf(minChargingPeriod)));
                    overTimeFee = overTimeFeeUnit.multiply(overTimePeriod);
                }
            }
            BigDecimal maxFee = lastRule.getMaxFee();
            if(null != maxFee && maxFee.compareTo(Constants.BIGDECIMAL_ZERO) > 0){
                data.setParkingFee((data.getParkingFee().compareTo(maxFee) >= 0) ? maxFee : data.getParkingFee());
            }
        }
        parkingOrder.setTotalFee(data.getParkingFee().setScale(2, BigDecimal.ROUND_HALF_UP));
        parkingOrder.setOvertimeFee(overTimeFee.setScale(2, BigDecimal.ROUND_HALF_UP));
        return data.getParkingFee().setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 计算免费时常
     * @param data
     * @param feeRuleParking
     * @param realEndTime
     * @param endTime
     * @return
     */
    private Boolean getFreeTime(ParkingRuleData data,FeeRuleParking feeRuleParking,Long realEndTime,Long endTime){
        Integer freeUseTime;
        //如果免费时间为空,为第一次匹配到规则,需要计入免费时间
        if(null == data.getFreeUseTime()){
            freeUseTime = feeRuleParking.getFreeUseTime();
            freeUseTime = (null == freeUseTime) ? 0 : freeUseTime;
            data.setFreeUseTime(freeUseTime);
            Long freeUseTimeLong = TimeUtil.minutesToMilliSeconds(freeUseTime);
            Long freeUseTimeEnd = freeUseTimeLong+data.getTimePointer();
            //免费结束时间超过了离场时间，整个费用计算没有产生费用
            if(freeUseTimeEnd >= realEndTime){
                //时间指针指向离场时间
                data.setTimePointer(realEndTime);
                return true;
            }
            //如果有结束时间(基础规则没有时间段)
            if(null != endTime){
                Long endTimeSameDayWithFreeUseTimeEnd = TimeUtil.changeTime3day(endTime,freeUseTimeEnd);
                //如果免费结束时间超过了时间段的结束时间，本次规则计算没有产生费用
                if(freeUseTimeEnd > endTimeSameDayWithFreeUseTimeEnd){
                    //时间指针指向免费结束时间
                    data.setTimePointer(freeUseTimeEnd);
                    return true;
                }
            }else{//反之没有结束时间
                data.setTimePointer(freeUseTimeEnd);
            }

        }
        return false;
    }


    /**
     * 首小时费用
     * @param data
     * @param feeRuleParking
     * @param realStartTime
     * @param realEndTime
     * @param endTime
     * @return
     */
    private Boolean getFirstHourFee(ParkingRuleData data,FeeRuleParking feeRuleParking,Long realStartTime,Long realEndTime,Long endTime){
        BigDecimal firstHourFee;
        //如果首小时费用为空，需要计算首小时费用
        if(null == data.getFirstHourFee()){
            firstHourFee = (null == feeRuleParking.getFirstHourFee()) ? Constants.BIGDECIMAL_ZERO : feeRuleParking.getFirstHourFee();
            data.setFirstHourFee(firstHourFee);
            //如果存在首小时费用，计算首小时费用
            if(firstHourFee.compareTo(Constants.BIGDECIMAL_ZERO) == 1){
                Long firstHourTimeEnd;
                //如果免费时间大于等于一个小时，首小时收费从免费时间结束之后开始计算
                if(null != data.getFreeUseTime() && data.getFreeUseTime() >= 60){
                    firstHourTimeEnd = data.getTimePointer() + TimeUtil.minutesToMilliSeconds(60);
                }else{//反之，首小时收费从停车开始时间之后开始计算
                    firstHourTimeEnd = realStartTime + TimeUtil.minutesToMilliSeconds(60);
                }
                //首小时收费结束时间超过了离场时间
                if(firstHourTimeEnd >= realEndTime){
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
                if(null != endTime){
                    Long endTimeSameDayWithFirstHourTimeEnd  = TimeUtil.changeTime3day(endTime,firstHourTimeEnd);
                    //首小时收费结束时间超过了时间段结束时间
                    if(firstHourTimeEnd >= endTimeSameDayWithFirstHourTimeEnd){
                        return true;
                    }
                }

            }
        }
        return false;
    }

    /**
     * 计算次小时费用
     * @param data
     * @param feeRuleParking
     * @param endTime
     * @param realEndTime
     * @return
     */
    private Boolean getOtherTimeFee(ParkingRuleData data,FeeRuleParking feeRuleParking,Long endTime,Long realEndTime){
        BigDecimal otherTimeFee = feeRuleParking.getOtherTimeFee();
        Long differenceTime;
        if(null != endTime){
            Long endTimeSameDayWithTimePointer  = TimeUtil.changeTime3day(endTime,data.getTimePointer());
            //如果实际离开时间小于时间段结束时间
            if(realEndTime <= endTimeSameDayWithTimePointer){
                differenceTime =  realEndTime - data.getTimePointer();
            }else{
                differenceTime =  endTimeSameDayWithTimePointer - data.getTimePointer();
            }
        }else{
            differenceTime =  realEndTime - data.getTimePointer();
        }
        BigDecimal otherTimeFeePeriod = new BigDecimal(TimeUtil.getPeriod(differenceTime, feeRuleParking.getOtherTimePeriod()));
        data.setParkingFee(data.getParkingFee().add(otherTimeFee.multiply(otherTimeFeePeriod)));
        //时间指针指向收过费用的时间
        data.setTimePointer(data.getTimePointer() + otherTimeFeePeriod.intValue() * TimeUtil.minutesToMilliSeconds(Integer.valueOf(feeRuleParking.getOtherTimePeriod())));
        return true;
    }


    /**
     * 查询下个规则时间段的开始时间
     * @param realEndTime
     * @param timePointer
     * @param infoData
     * @return
     */
    private Long findNextFeeRuleStartTime(Long realEndTime, Long timePointer,ParkingFeeRuleInfoData infoData){
        Integer type = infoData.getType();
        List<FeeRuleParking> feeRuleParkings = infoData.getFeeRuleParkings();
        for(FeeRuleParking feeRuleParking : feeRuleParkings){
            Long startTime = feeRuleParking.getStartTime();
            Long endTime = feeRuleParking.getEndTime();
            Integer weekDay = feeRuleParking.getWeekday();
            if(null != startTime){
                //遍历改规则所有时间段
                int day = TimeUtil.differentDaysByMillisecond(startTime,endTime);
                for(int i = 0;i <= day;i++){
                    Long startTimeTemp = startTime+i*Constants.TIMESTAMP_ONE_DAY;
                    Long endTimeTemp = endTime-(day-i)*Constants.TIMESTAMP_ONE_DAY;
                    //如果规则分为工作日/周末
                    if(1 == type){
                        int dayOfWeek = TimeUtil.isWeekDay(startTimeTemp) ? 1 : 0;
                        if(weekDay != dayOfWeek){
                            continue;
                        }
                    }
                    //如果和停车时间相交，返回下个规则时间段的开始时间
                    if(!(timePointer > endTimeTemp || realEndTime <startTimeTemp)){
                        return startTimeTemp;
                    }
                }
            }
        }
        return null;
    }

    /**
     *  计算费用
     * @param feeRuleParking
//     * @param startTime 实际进场时间
//     * @param endTime 没超时：实际离场的时间， 超时：预约结束的时间
     * @param parkingTime 停车时间
     * @param index 工作日：1，周末：0
     * @param overtime 超时时间
     * @param minChargingPeriod 最短计费时间
     * @param isAllOvertime 是否全部是超时时段
     * @return
     */
    public BigDecimal calculate(FeeRuleParking feeRuleParking, Long parkingTime, Long overtime, Integer index, Integer minChargingPeriod,
                                boolean isAllOvertime) {
        BigDecimal fee = BigDecimal.ZERO;

        /*//        Integer index = TimeUtil.isWeekDay(System.currentTimeMillis()) ? 1 : 0;
        BigDecimal firstHourFee = new BigDecimal(feeRuleParking.getFirstHourFee().split(Constants.DELIMITER_COMMA)[index]);
        BigDecimal otherTimeFee = new BigDecimal(feeRuleParking.getOtherTimeFee().split(Constants.DELIMITER_COMMA)[index]);
        BigDecimal maxFee = new BigDecimal(feeRuleParking.getMaxFee().split(Constants.DELIMITER_COMMA)[index]);
        Long freeUseTime = TimeUtil.muniteToMilliSeconds(Integer.valueOf(feeRuleParking.getFreeUseTime().split(Constants.DELIMITER_COMMA)[index]));
        BigDecimal overTimeFee = new BigDecimal(feeRuleParking.getOverTimeFee().split(Constants.DELIMITER_COMMA)[index]);
        log.info("周末：0，工作日：1 --> " + index + ", firstHourFee=" + firstHourFee + ", otherTimeFee=" + otherTimeFee + ", maxFee = " + maxFee
                + ", freeUseTime=" + freeUseTime + ", overTimeFee=" + overTimeFee + ", isAllOvertime = " + isAllOvertime);

        if (isAllOvertime) {
            BigDecimal overTimePeriod = new BigDecimal(TimeUtil.getPeriod(overtime, minChargingPeriod));
            log.info("全部是超时时段，isAllOvertime = " + isAllOvertime + ", overTimePeriod = " + overTimePeriod + ", 超时最小计费时长 minChargingPeriod = " + minChargingPeriod);
            fee = overTimeFee.multiply(overTimePeriod);

        } else {
            // 超时情况
            if (overtime > 0) {
                BigDecimal overTimePeriod = new BigDecimal(TimeUtil.getPeriod(overtime, minChargingPeriod));

                int hours = TimeUtil.milliSecondsToHours(parkingTime);
                log.info("停车时长 hours=" + hours + ", overTimePeriod = " + overTimePeriod + ", 超时最小计费时长 minChargingPeriod = " + minChargingPeriod);
                // 未超时的停车时长超过一个小时，要算次小时费用
                if (hours > 1) {
                    fee = firstHourFee.add(otherTimeFee.multiply(new BigDecimal(hours - 1))).add(overTimeFee.multiply(overTimePeriod));
                } else {
                    fee = firstHourFee.add(overTimeFee.multiply(overTimePeriod));
                }

            } else {
                int hours = TimeUtil.milliSecondsToHours(parkingTime);
                log.info("停车时长 hours=" + hours);
                if (hours > 1) {
                    fee = firstHourFee.add(otherTimeFee.multiply(new BigDecimal(hours - 1)));
                } else {
                    fee = firstHourFee;
                }
            }
        }*/
        return fee.setScale(2, BigDecimal.ROUND_HALF_UP);
    }


    /**
     * 释放车位
     * @param parkingOrder 订单
     */
    private void releaseParkloc(OrderParking parkingOrder) {
        log.info("release parkloc start");
        Parkloc parkloc = parklocRepository.findOne(parkingOrder.getParklocId());
        Long num = shareMapper.countParklocReservable();
        log.info("current available reserve time has {}", num);
        if (null != num && num > Constants.EMPTY_CAPACITY) {
            parkloc.setState(Status.Parkloc.PUBLISHED.getInt());
        }else{
            parkloc.setState(Status.Parkloc.UNPUBLISHED.getInt());
        }
        parklocRepository.save(parkloc);
    }


    public void dealWithToBePublish(OrderParking orderParking){
        log.info("开始执行处理待修改待取消线程");
        if(null == orderParking){
            log.info("无效的orderparking对象");
            return;
        }
        Integer reservationId = orderParking.getReservationId();
        Integer parklocId = orderParking.getParklocId();
        Publish publish = publishRepository.findByReservationId(reservationId);
        if(null == publish){
            log.info("无效的publish对象");
            return;
        }
        Integer publishId = publish.getId();
        Integer publishState = publish.getState();
        log.info("publishId is {},publishState is {}",publishId,publishState);
        //如果是待取消
        if(Status.Publish.TOBECANCELLED.getInt().equals(publishState)){
            log.info("进入待取消");
            //发布状态改为失效
            publishRepository.updateState(publishId,Status.Common.INVALID.getInt());
            shareRepository.updateState(publishId,Status.Common.INVALID.getInt(),Status.Common.VALID.getInt());
            //如果车位没有发布的车位，更新车位状态
            List<Integer> states = new ArrayList<>();
            states.add(Status.Publish.TOBEALTER.getInt());
            states.add(Status.Publish.TOBECANCELLED.getInt());
            states.add(Status.Common.VALID.getInt());
            List<Publish> publishList = publishRepository.findByParklocIdAndStates(parklocId,states);
            if(publishList.size() == Constants.MIN_NON_NEGATIVE_INTEGER){
                parklocRepository.updateState(parklocId,Status.Parkloc.UNPUBLISHED.getInt());
            }
        }
        //如果是待修改
        else if(Status.Publish.TOBEALTER.getInt().equals(publishState)){
            log.info("进入待修改");
            //共享状态改为失效
            shareRepository.updateState(publishId,Status.Common.INVALID.getInt(),Status.Common.VALID.getInt());
            //把发布状态改为有效
            publishRepository.updateState(publishId,Status.Common.VALID.getInt());
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
            this.share(publish.getMode(),parklocId,publishId,startTime,endTime,publish.getDayOfWeek(),todayOfWeek);
        }

        // 更新该车场的parklot_amount表的数据
        Integer parklotId = orderParking.getParklotId();
        parklotAmountService.updateParklotAmountInfoByParklotId(parklotId, "dealWithToBePublish 停车离场");
    }


    private void share(Integer mode,Integer parklocId,Integer publishId,Long startTime,Long endTime,String dayOfWeek,int todayOfWeek){
        //如果不是重复发布直接插入到Share表
        Share share;
        if(Constants.SINGLE_MODE.equals(mode)){
            share = new Share(parklocId,publishId,startTime,endTime,Status.Common.VALID.getInt());
            if(null == shareRepository.save(share)){
                log.error("parklocId:"+parklocId +" result:Share "+Status.ApiErr.INSERT_ERROR.getMsg());
                throw new com.qhieco.apiservice.exception.QhieException(Status.ApiErr.INSERT_ERROR);
            }
        }else{
            //如果重复发布中包含了今天插入到Share表
            if (dayOfWeek.contains(String.valueOf(todayOfWeek))) {
                share = new Share(parklocId,publishId,startTime,endTime,Status.Common.VALID.getInt());
                if(null == shareRepository.save(share)){
                    log.error("parklocId:"+parklocId +" result:Share "+Status.ApiErr.INSERT_ERROR.getMsg());
                    throw new com.qhieco.apiservice.exception.QhieException(Status.ApiErr.INSERT_ERROR);
                }
            }
        }
    }


    /**
     * 临时卡登记功能
     * @param orderParking 预约订单
     */
    private void registeredTemp(OrderParking orderParking) {
        try {
            log.info("零时卡登记orderParkingId:{}",orderParking.getId());
            Integer plateId = orderParking.getPlateId();
            Integer parklotId = orderParking.getParklotId();
            Plate plate = plateRepository.findOne(plateId);
            Long now = System.currentTimeMillis();
            List<String> params = new  ArrayList<>();
            String accessCode = configuration.getBoostedGoalAccessCode();
            String plateNumber = plate.getNumber();
            String beginDate = TimeUtil.timestampToStr(now);
            params.add("AccessCode="+accessCode);
            params.add("BusinessCode="+"RP0001");
            params.add("SignType="+"SHAONE");
            params.add("PlateNumber="+plateNumber);
            //查询车场在对应表中的Id
            Parklot parklot = parklotRepository.findOne(parklotId);
            String extraParklotId = parklot.getExtraParklotId();
            log.info("extraParklotId:{}",extraParklotId);
            params.add("ParkingID="+extraParklotId);
            params.add("BeginDate="+beginDate);
            String endDate = TimeUtil.timestampToStr(now + 1000);
            params.add("EndDate="+endDate);
            params.add("Secretkey="+configuration.getBoostedGoalSecretKey());
            String sign = EncryptUtil.signature(params);
            BoostedGoalParkingRequest request = new BoostedGoalParkingRequest(accessCode,"RP0001","SHAONE",sign,plateNumber,parklot.getExtraParklotId().toString(),beginDate,endDate);
            Gson gson = new Gson();
            String json = "data="+gson.toJson(request);
            String url = configuration.getBoostedGoalParkingUrl();
            this.registeredTemp(json,url,Constants.APPLICATION_FORM);
        }catch(Exception e){
            e.printStackTrace();
        }

    }


    public void registeredTemp(String jsonContent,String url,String contentType) {
        StringHttpMessageConverter m = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        RestTemplate restTemplate = new RestTemplateBuilder().additionalMessageConverters(m).build();
        HttpHeaders httpHeaders = new HttpHeaders();
        MediaType type = MediaType.parseMediaType(contentType);
        httpHeaders.setContentType(type);
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonContent, httpHeaders);
        log.info("register temp request: {}", url + " " + jsonContent);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        log.info("register temp response is {}", response);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp postCarInInfo(PostCarInfoRequest request){
        if(!Constants.KEY_TOP_APPID.equals(request.getAppId())){
            throw new QhieException(Status.ApiErr.APP_ID_ERROR);
        }
        Long enterTime = TimeUtil.dateTotamp(request.getEnteyTime());
        if(null == enterTime){
            throw new QhieException(Status.ApiErr.TIME_ILLEGAL);
        }
        String enterStr = TimeUtil.timestampToStr2(enterTime);
        String secret = Constants.KEY_TOP_SECRET;
        //验证KEY是否合法
        String localKey = EncryptUtil.md5Password(enterStr+secret);
        if(!localKey.equals(request.getKey())){
            throw new QhieException(Status.ApiErr.KEY_ERROR);
        }
        Integer parkId = request.getParkId();
        Parklot parklot = parklotRepository.findByExtraParklotId(parkId.toString());
        if(null == parklot){
            throw new QhieException(Status.ApiErr.NONEXISTENT_PARKLOT);
        }
        Integer parklotId = parklot.getId();
        String plateNum = request.getPlateNo();
        log.info("plateNum is {}", plateNum);
        Integer plateId = plateRepository.findByNumber(plateNum, Status.Common.VALID.getInt());
        log.info("plateId is {}, parklot id is {}", plateId, parklotId);
        List<OrderParking> parkingOrders = orderParkingRepository.findByParklotIdAndPlateIdAndStateOrderByIdDesc(parklotId, plateId, Status.OrderParking.USED.getInt());
        log.info("park order is {}", parkingOrders);
        if (null != parkingOrders && parkingOrders.size() > Constants.EMPTY_CAPACITY) {
            throw new QhieException(Status.ApiErr.NONEXISTENT_ORDER_PARKING);
        }
        log.info("finding reserve order");
        List<OrderParking> reserveOrders = orderParkingRepository.findByParklotIdAndPlateIdAndStateOrderByIdDesc(parklotId, plateId, Status.OrderParking.RESERVED.getInt());
        if (null == reserveOrders || Constants.EMPTY_CAPACITY == reserveOrders.size()) {
            throw new QhieException(Status.ApiErr.NONEXISTENT_ORDER_RESERVE_PARKING);
        }
        log.info("find the latest reserve order");
        OrderParking reserveOrder = reserveOrders.get(Constants.FIRST_INDEX);
        parkingOrders = orderParkingRepository.findByParklotIdAndPlateIdAndStateOrderByIdDesc(parklotId, plateId, Status.OrderParking.PAID.getInt());
        log.info("find payed order");
        if (null != parkingOrders && parkingOrders.size() > Constants.EMPTY_CAPACITY) {
            OrderParking orderParking = parkingOrders.get(Constants.FIRST_INDEX);
            if (orderParking.getId() > reserveOrder.getId()) {
                throw new QhieException(Status.ApiErr.REPEAT_PARKING);
            }
        }
        log.info("reserve order is {}",reserveOrder);
        BigDecimal initialMoney = new BigDecimal(0.00);
        OrderParking orderParking = new OrderParking();
        orderParking.setReserveId(reserveOrder.getId());
        orderParking.setSerialNumber(OrderNoGenerator.getOrderNo(Status.OrderType.TYPE_PARKING.getInt(), String.valueOf(reserveOrder.getMobileUserId())));
        orderParking.setMobileUserId(reserveOrder.getMobileUserId());
        orderParking.setReservationId(reserveOrder.getReservationId());
        orderParking.setParklocId(reserveOrder.getParklocId());
        orderParking.setParklotId(reserveOrder.getParklotId());
        orderParking.setPlateId(reserveOrder.getPlateId());
        orderParking.setRealStartTime(enterTime);
        orderParking.setTotalFee(initialMoney);
        orderParking.setDiscountFee(initialMoney);
        orderParking.setRealFee(initialMoney);
        orderParking.setTripartiteFee(initialMoney);
        orderParking.setPlatformIncome(initialMoney);
        orderParking.setOwnerIncome(initialMoney);
        orderParking.setManageIncome(initialMoney);
        orderParking.setCreateTime(System.currentTimeMillis());
        orderParking.setState(Status.OrderParking.USED.getInt());
        orderParking.setOvertimeFee(Constants.BIGDECIMAL_ZERO);
        log.info("save the reserve order");
        OrderParking orderParkingResp = orderParkingRepository.save(orderParking);
        if (null == orderParkingResp) {
            throw new QhieException(Status.ApiErr.INSERT_ERROR);
        }
        //保存到订单总表
        OrderTotal orderTotal = new OrderTotal(reserveOrder.getMobileUserId(),orderParking.getSerialNumber(),Constants.PARKING_ORDER,orderParking.getCreateTime(),Constants.BIGDECIMAL_ZERO,Status.OrderParking.USED.getInt());
        orderTotalRepository.save(orderTotal);
        UserExtraInfo userExtraInfo = userExtraInfoRepository.findByMobileUserId(reserveOrder.getMobileUserId());
        if(null == userExtraInfo || StringUtils.isEmpty(userExtraInfo.getJpushRegId())){
            return RespUtil.successResp();
        }
        log.info("push to user id: " + reserveOrder.getMobileUserId());
        //给前端发送自定义消息提示
        QhPushUtil.getInstance().sendQhPush(userExtraInfo.getJpushRegId(), QhMessageType.CUSTOM, QhMessageTemplate.ENTER,
                orderParkingResp.getId().toString());
        return RespUtil.successResp();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp postCarOutInfo(PostCarInfoRequest request) {
        if(!Constants.KEY_TOP_APPID.equals(request.getAppId())){
            throw new QhieException(Status.ApiErr.APP_ID_ERROR);
        }
        Long realStartTime = TimeUtil.dateTotamp(request.getEnteyTime());
        if(null == realStartTime){
            throw new QhieException(Status.ApiErr.TIME_ILLEGAL);
        }
        Long realEndTime = TimeUtil.dateTotamp(request.getLeaveTime());
        if(null == realEndTime){
            throw new QhieException(Status.ApiErr.TIME_ILLEGAL);
        }
        String leaveStr = TimeUtil.timestampToStr2(realEndTime);
        String secret = Constants.KEY_TOP_SECRET;
        //验证KEY是否合法
        String localKey = EncryptUtil.md5Password(leaveStr+secret);
        if(!localKey.equals(request.getKey())){
            throw new QhieException(Status.ApiErr.KEY_ERROR);
        }
        Integer parkId = request.getParkId();
        Parklot parklot = parklotRepository.findByExtraParklotId(parkId.toString());
        Integer parklotId = parklot.getId();
        Integer allocable = parklot.getAllocable();
        String plateNum = request.getPlateNo();
        Integer plateId = plateRepository.findByNumber(plateNum, Status.Common.VALID.getInt());
        List<OrderParking> parkingOrders = orderParkingRepository.findByParklotIdAndPlateIdAndStateOrderByIdDesc(parklotId, plateId, Status.OrderParking.PAID.getInt());
        if (checkIfRepeat(realStartTime, parkingOrders)) {
            throw new QhieException(Status.ApiErr.REPEAT_LEAVE);
        }
        log.info("plate id is {}, parklot id is {}", plateId, parklotId);
        parkingOrders = orderParkingRepository.findByParklotIdAndPlateIdAndStateOrderByIdDesc(parklotId, plateId, Status.OrderParking.USED.getInt());
        if(null == parkingOrders || Constants.EMPTY_CAPACITY == parkingOrders.size()){
            log.info("未查询到对应的停车订单。");
            throw new QhieException(Status.ApiErr.NONEXISTENT_PARKING_ORDER);
        }
        OrderParking parkingOrder = parkingOrders.get(Constants.FIRST_INDEX);
        log.info("last used parkingOrder: {}", parkingOrder);
        /** *   *   *  此段代码为测试使用我们系统的停车计费规则计算费用 start*   *   */
        BigDecimal tempTotalFee = Constants.BIGDECIMAL_ONE;
        try {
            tempTotalFee = calculateParkingTotalFee(realEndTime, parkingOrder);
            log.info("使用我们系统的停车计费规则 计算费用为 ：tempTotalFee = " + tempTotalFee);
        } catch (Exception e) {
            log.error("计算停车费用异常， " + e);
        }
        /** *   *   *  此段代码为测试使用我们系统的停车计费规则计算费用 end*   *   */

        //没有未付金额
        if(Constants.BIGDECIMAL_ZERO.equals(tempTotalFee)){
            OrderParking orderParkingResp = updateParkingState(Constants.BIGDECIMAL_ZERO,Constants.BIGDECIMAL_ZERO, realEndTime, parkingOrder,allocable);
            //更新预约订单的可开票状态
            Integer reserveOrderId = parkingOrder.getReserveId();
            orderParkingRepository.updateInvoiceState(reserveOrderId,Status.InvoiceStatus.UNMAKE.getInt());
            //更新用户的可开票金额（预约记录开票）
            OrderParking reserveOrder = orderParkingRepository.findOne(reserveOrderId);
            balanceUserRepository.updateBalanceInvoice(reserveOrder.getRealFee(),parkingOrder.getMobileUserId());
            //更新车位所有者分成(预约分成)
            balanceUserRepository.updateBalanceEarnByParklocId(reserveOrder.getParklocId(),reserveOrder.getOwnerIncome());
            //更新停车场分成（预约分成）
            balanceParklotRepository.updateBalanceByParklotId(reserveOrder.getParklotId(),reserveOrder.getManageIncome());
        }else{
            //存在未支付金额
            parkingOrder.setTotalFee(tempTotalFee.divide(Constants.BIGDECIMAL_ONE_HUNDRED, QhieCurrency.DEFAULT_SCALE, RoundingMode.HALF_UP));
            parkingOrder.setState(Status.OrderParking.UNPAID.getInt());
            parkingOrder.setRealEndTime(realEndTime);
            Reservation reservation = reservationRepository.findOne(parkingOrder.getReservationId());
            if (null != realEndTime && realEndTime > reservation.getEndTime()) {
                parkingOrder.setOvertime(realEndTime - reservation.getEndTime());
            }
            orderParkingRepository.save(parkingOrder);
        }

        releaseParkloc(parkingOrder);
        //线程处理待取消待发布
        dealWithToBePublish(parkingOrder);
        Integer barrierManufacturer = parklot.getBarrierManufacturer();
        //删除白名单
        if(Constants.BARRIER_MANUFACTURER_KEY_TOP.equals(barrierManufacturer)){
            this.cancelRegistered(parkingOrder);
        }
        /*
         * 推送信息
         */
        UserExtraInfo userExtraInfo = userExtraInfoRepository.findByMobileUserId(parkingOrder.getMobileUserId());
        if(null == userExtraInfo || StringUtils.isEmpty(userExtraInfo.getJpushRegId())){
            return RespUtil.successResp();
        }
        //给前端发送自定义消息提示
        QhPushUtil.getInstance().sendQhPush(userExtraInfo.getJpushRegId(), QhMessageType.CUSTOM, QhMessageTemplate.LEAVE,
                parkingOrder.getId().toString());
        return RespUtil.successResp();
    }


    private void cancelRegistered(OrderParking orderParking){
        String orderNo = orderParking.getThirdPartyNo();
        Integer parklotId = orderParking.getParklotId();
        String extraParklotId = parklotRepository.findOne(parklotId).getExtraParklotId();
        if(null == orderNo || null == extraParklotId){
            return;
        }
        Long now = System.currentTimeMillis();
        String appid = configuration.getKeyTopAppId();
        String keyTopSecretKey = configuration.getKeyTopSecretKey();
        String keyTopParkingUrl = configuration.getKeyTopCancelUrl();
        String key = EncryptUtil.md5Password(extraParklotId+orderNo+TimeUtil.timestampToStr2(now)+keyTopSecretKey);
        KeyTopCancelRequest request = new KeyTopCancelRequest(Integer.valueOf(appid),key,Integer.valueOf(extraParklotId),orderNo);
        Gson gson = new Gson();
        String json = gson.toJson(request);
        this.registeredTemp(json,keyTopParkingUrl,Constants.APPLICATION_JSON);
    }

    @Override
    public Resp getFee(){
        OrderParking orderParking = orderParkingRepository.getOne(44135);
        BigDecimal totalFee  = this.calculateParkingTotalFee(1531820142984L,orderParking);
        return RespUtil.successResp(orderParking);
    }



}

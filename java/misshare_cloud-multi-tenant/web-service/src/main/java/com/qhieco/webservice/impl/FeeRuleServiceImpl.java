package com.qhieco.webservice.impl;

import com.qhieco.anotation.AddTenantInfo;
import com.qhieco.anotation.EnableTenantFilter;
import com.qhieco.commonentity.*;
import com.qhieco.commonentity.relational.ParklotFeeRuleParkingB;
import com.qhieco.commonentity.relational.ParklotFeeRuleReserveB;
import com.qhieco.commonentity.relational.ParklotParamsB;
import com.qhieco.commonrepo.*;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.api.ParkingRuleData;
import com.qhieco.request.web.BindFeeRuleRequest;
import com.qhieco.request.web.FeeRequest;
import com.qhieco.request.web.FeeRuleAddParkingRequest;
import com.qhieco.request.web.FeeRuleAddReserveRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.AbstractPaged;
import com.qhieco.response.data.web.ParkingFeeRuleInfoData;
import com.qhieco.util.PageableUtil;
import com.qhieco.util.RespUtil;
import com.qhieco.util.TenantContext;
import com.qhieco.util.TimeUtil;
import com.qhieco.webmapper.FeeRuleMapper;
import com.qhieco.webmapper.ParklotParamsMapper;
import com.qhieco.webservice.FeeRuleService;
import com.qhieco.webservice.exception.QhieWebException;
import com.qhieco.webservice.utils.QueryFunction;
import com.qhieco.util.TenantHelper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/27 下午4:17
 * <p>
 * 类说明：
 *     费用规则具体实现
 */
@Service
@Slf4j
public class FeeRuleServiceImpl implements FeeRuleService {

    @Autowired
    private FeeRuleReserveRepository feeRuleReserveRepository;

    @Autowired
    private FeeRuleParkingRepository feeRuleParkingRepository;

    @Autowired
    private FeeRuleParkingBaseRepository feeRuleParkingBaseRepository;

    @Autowired
    private ParklotFeeRuleReserveBRepository parklotFeeRuleReserveBRepository;

    @Autowired
    private ParklotFeeRuleParkingBRepository parklotFeeRuleParkingBRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ParklotParamsMapper parklotParamsMapper;

    @Autowired
    private FeeRuleMapper feeRuleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @EnableTenantFilter
    public Resp addParkingFee(FeeRuleAddParkingRequest feeRuleAddParkingRequest) {
        Long now = System.currentTimeMillis();
        Integer id = feeRuleAddParkingRequest.getId();
        String name = feeRuleAddParkingRequest.getName();
        Integer type = feeRuleAddParkingRequest.getType();
        List<FeeRuleParkingBase> feeRuleParkingList;
        FeeRuleParkingBase feeRuleParkingBase = new FeeRuleParkingBase(name,type,now,Status.Common.VALID.getInt());
        if(null != id && id != Constants.MIN_NON_NEGATIVE_INTEGER){
            feeRuleParkingList = feeRuleParkingBaseRepository.findByNameExceptMyself(name,id);
            FeeRuleParkingBase feeRuleParkingBaseDB = feeRuleParkingBaseRepository.getOne(id);
            feeRuleParkingBase.setId(id);
            feeRuleParkingBase.setCreateTime(feeRuleParkingBaseDB.getCreateTime());
            //将之前的关联关系改为失效
            feeRuleParkingRepository.updateStateAndUpdateTimeByFeeRuleParkingBaseId(id,Status.Common.INVALID.getInt(),now);
        }else{
            feeRuleParkingList = feeRuleParkingBaseRepository.findByName(name);
        }
        log.info("exist name fee base num:{}",feeRuleParkingList.size());
        //判断名称是否重复
        if (null != feeRuleParkingList && Constants.EMPTY_CAPACITY != feeRuleParkingList.size()) {
            throw new QhieWebException(Status.WebErr.FEE_RULE_NAME_EXISTS);
        }
        //保存停车收费规则基础信息表
        FeeRuleParkingBase feeRuleParkingBaseResp = feeRuleParkingBaseRepository.save(feeRuleParkingBase);
        if (null == feeRuleParkingBaseResp) {
            throw new QhieWebException(Status.WebErr.INSERT_ERROR);
        }
        List<FeeRuleParking> feeRuleParkings = feeRuleAddParkingRequest.getFeeRuleParkings();
        Boolean haveBaseRule = false;
        //循环保存规则
        if(null != feeRuleParkings && feeRuleParkings.size() > Constants.EMPTY_CAPACITY){
            for(FeeRuleParking feeRuleParking :feeRuleParkings){
                Integer weekDay = feeRuleParking.getWeekday();
                BigDecimal otherTimeFee = feeRuleParking.getOtherTimeFee();
                Long startTime = feeRuleParking.getStartTime();
                Long endTime = feeRuleParking.getEndTime();
                if(null != startTime && null != endTime){
                    Long startTimeZero = TimeUtil.initDateByDay(startTime);
                    Long endTimeZero = TimeUtil.initDateByDay(endTime);
                    if(endTimeZero < startTimeZero){
                        throw new QhieWebException(Status.WebErr.PARAM_ERROR);
                    }
                }

                if(null == weekDay || null == otherTimeFee){
                    throw new QhieWebException(Status.WebErr.PARAM_ERROR);
                }else{
                    if(null == feeRuleParking.getStartTime()){
                        haveBaseRule = true;
                    }
                    feeRuleParking.setFeeRuleParkingBaseId(feeRuleParkingBaseResp.getId());
                    feeRuleParking.setState(Status.Common.VALID.getInt());
                    feeRuleParking.setUpdateTime(now);
                    FeeRuleParking feeRuleParkingResp = feeRuleParkingRepository.save(feeRuleParking);
                    if (null == feeRuleParkingResp) {
                        throw new QhieWebException(Status.WebErr.INSERT_ERROR);
                    }
                }
            }
        }else{
            throw new QhieWebException(Status.WebErr.PARAM_ERROR);
        }


        //循环比较时间段是否重复
        for(FeeRuleParking feeRuleParking1 : feeRuleParkings){
            Long startTime1 = feeRuleParking1.getStartTime();
            Long endTime1 = feeRuleParking1.getEndTime();
            //出现重复次数，出现两次时返回错误给前端
            Integer repeat = 0;
            if(null == startTime1){
                continue;
            }
            //把外层时间段放在同一天，如果结束时间小于开始时间，结束时间天数+1
            Long startTimeOneDay1 = TimeUtil.formatOneDayTime(startTime1);
            Long endTimeOneDay1 = TimeUtil.formatOneDayTime(endTime1);
            if(endTimeOneDay1 <= startTimeOneDay1){
                endTimeOneDay1 = endTimeOneDay1+Constants.TIMESTAMP_ONE_DAY;
            }
            //获取时间段的零点的时间、
            Long startTimeZero1 = TimeUtil.initDateByDay(startTime1);
            Long endTimeZero1 = TimeUtil.initDateByDay(endTime1);
            Long timeCount = 0L;
            for(FeeRuleParking feeRuleParking2 : feeRuleParkings){
                Long startTime2 = feeRuleParking2.getStartTime();
                Long endTime2 = feeRuleParking2.getEndTime();
                if(null == startTime2){
                    continue;
                }
                //把内层时间放到同一天，如果结束时间小于开始时间，结束时间天数+1
                Long startTimeOneDay2 = TimeUtil.formatOneDayTime(startTime2);
                Long endTimeOneDa2 = TimeUtil.formatOneDayTime(endTime2);
                if(endTimeOneDa2 <= startTimeOneDay2){
                    endTimeOneDa2 = endTimeOneDa2+Constants.TIMESTAMP_ONE_DAY;
                }
                //获取时间段的零点的时间、
                Long startTimeZero2 = TimeUtil.initDateByDay(startTime2);
                Long endTimeZero2 = TimeUtil.initDateByDay(endTime2);
                //先判断有效期间是否重合
                if(!(endTimeZero1 < startTimeZero2 || startTimeZero1 > endTimeZero2)){
                    timeCount =  timeCount + (endTimeOneDa2 - startTimeOneDay2);
                    //判断是否时间段存在重合
                    if(!(endTimeOneDay1 < startTimeOneDay2 || startTimeOneDay1 > endTimeOneDa2)){
                        repeat = repeat + 1;
                    }
                }
                if(timeCount > Constants.TIMESTAMP_ONE_DAY){
                    log.info("所有时段之和最大为24小时");
                    throw new QhieWebException(Status.WebErr.FEE_RULE_PARKING_COUNT_TIME_ERROR);
                }

            }
            if(repeat == 2){
                log.info("计费时段重复，请重新提交");
                throw new QhieWebException(Status.WebErr.FEE_RULE_PARKING_TIME_DUPLICATE);
            }
        }




        if(!haveBaseRule){
            log.info("没有基本的收费规则");
            throw new QhieWebException(Status.WebErr.PARAM_ERROR);
        }
        return RespUtil.successResp();
    }

    @Override
    @Transactional
    @EnableTenantFilter
    public Resp addReserveFee(FeeRuleAddReserveRequest feeRuleAddReserveRequest) {
        final String name = feeRuleAddReserveRequest.getName();
        List<FeeRuleReserve> feeRuleReserveList = feeRuleReserveRepository.findByName(name);
        if (null != feeRuleReserveList && Constants.EMPTY_CAPACITY != feeRuleReserveList.size()) {
            throw new QhieWebException(Status.WebErr.FEE_RULE_NAME_EXISTS);
        }
        String advanceTimeList = feeRuleAddReserveRequest.getAdvanceTimeList();
        String feeList = feeRuleAddReserveRequest.getFeeList();
        FeeRuleReserve feeRuleReserve = new FeeRuleReserve(name, feeList, advanceTimeList, Status.Common.VALID.getInt());
        if (null == feeRuleReserveRepository.save(feeRuleReserve)) {
            throw new QhieWebException(Status.WebErr.INSERT_ERROR);
        }
        return RespUtil.successResp();
    }

    @Override
    @Transactional
    public Resp bindFeeRule(BindFeeRuleRequest bindFeeRuleRequest) {
        Integer feeRuleType = bindFeeRuleRequest.getType();
        Integer parklotId = bindFeeRuleRequest.getParklotId();
        Integer feeRuleId = bindFeeRuleRequest.getFeeRuleId();
        if (Constants.FeeRuleType.RESERVE.ordinal() == feeRuleType) {
            ParklotFeeRuleReserveB parklotFeeRuleReserveB = new ParklotFeeRuleReserveB(parklotId, feeRuleId, Status.Common.VALID.getInt(), System.currentTimeMillis());
            if (null == parklotFeeRuleReserveBRepository.save(parklotFeeRuleReserveB)) {
                throw new QhieWebException(Status.WebErr.INSERT_ERROR);
            }
        } else if (Constants.FeeRuleType.PARKING_PER_HOUR.ordinal() == feeRuleType){
            ParklotFeeRuleParkingB parklotFeeRuleParkingB = new ParklotFeeRuleParkingB(parklotId, feeRuleId, Status.Common.VALID.getInt(), System.currentTimeMillis());
            if (null == parklotFeeRuleParkingBRepository.save(parklotFeeRuleParkingB)) {
                throw new QhieWebException(Status.WebErr.INSERT_ERROR);
            }
        } else {
            throw new QhieWebException(Status.WebErr.ILLEGAL_FEE_TYPE);
        }
        return RespUtil.successResp();
    }


    @Override
    public Resp queryParkingFee(FeeRequest request) {
        if(TenantContext.getCurrentTenant()!=null){
            request.setTenantId(TenantContext.getCurrentTenant());
        }
        int count = feeRuleMapper.queryCountParkingFeeRuleList(request);
        List<ParkingFeeRuleInfoData> parkingFeeRuleInfoDataList = null;
        if (count > 0) {
            parkingFeeRuleInfoDataList = feeRuleMapper.queryParkingFeeRuleList(request);
            TenantHelper.fillTenantName(parkingFeeRuleInfoDataList);
        }
        AbstractPaged<ParkingFeeRuleInfoData> data = AbstractPaged.<ParkingFeeRuleInfoData>builder()
                .sEcho(request.getSEcho() + 1)
                .dataList(parkingFeeRuleInfoDataList)
                .iTotalRecords(count)
                .iTotalDisplayRecords(count)
                .build();
        return RespUtil.successResp(data);
    }

    @Override
    @EnableTenantFilter
    @AddTenantInfo
    public Resp queryReserveFee(FeeRequest request){
        return QueryFunction.query(
                request,
                (root, query, cb) -> {
                    val page = new PageableUtil(root, query, cb);
                    //费用名称
                    page.like("name", request.getName());
                    return page.pridect();
                },
                feeRuleReserveRepository);
    }


    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
        return days;
    }

    // 判断两个时间是否在同一天
    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 != null && cal2 != null) {
            return cal1.get(0) == cal2.get(0) && cal1.get(1) == cal2.get(1) && cal1.get(6) == cal2.get(6);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    /**
     * 判断当前日期是星期几<br>
     * <br>
     * @param pTime 修要判断的时间<br>
     * @return dayForWeek 判断结果<br>
     * @Exception 发生异常<br>
     */
    public static int dayForWeek(Calendar pTime) throws Exception {
        int dayForWeek = 0;
        if (pTime.get(Calendar.DAY_OF_WEEK) == 1) {
            dayForWeek = 7;
        } else {
            dayForWeek = pTime.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

    @Override
    public void calculateFee(OrderParking orderParking){

        /*List<ParklotParamsB> parklotTimeParamsList = parklotParamsMapper.queryParklotParams(
                orderParking.getParklotId(), Status.Common.VALID.getInt(), Constants.TIME_CODE);
        final Integer[] chargePeriod = {60};
        parklotTimeParamsList.stream()
                .filter(param -> "min_charging_period".equalsIgnoreCase(param.getQhKey()))
                .findAny()
                .ifPresent(parklotParamsB -> chargePeriod[0] =Integer.parseInt(parklotParamsB.getQhValue()));
        Long now = 0L;
        if(orderParking.getState().equals(Status.OrderParking.USED.getInt())){
            now = System.currentTimeMillis();
        }else {
            return;
        }
        ParkingFeeRuleInfoData infoData = feeRuleMapper.queryParkingFeeRuleById(orderParking.getParklotId());
        Boolean over = now > orderParking.getEndTime();
        Integer stopTime = TimeUtil.milliSecondsToMinutes(now - orderParking.getRealStartTime());
        Integer totalFee = 0;
        Integer overFee = 0;
        Integer dayOfWeek = TimeUtil.getDayOfWeekToday();
        Integer weekday = dayOfWeek==0||dayOfWeek==6 ? 0:1;

        val anyFee =  feeRuleList.stream().filter(feeRuleParking -> feeRuleParking.getWeekday()
                .contains(String.valueOf(weekday))).findAny();
        if(over){
            stopTime = TimeUtil.milliSecondsToMinutes(orderParking.getEndTime()-orderParking.getRealStartTime());
        }
        Integer finalFee = 0;
        if(anyFee.isPresent()){
            val feeRule = anyFee.get();
            Integer freeTime = Integer.parseInt(feeRule.getFreeUseTime().split(",")[weekday]);
            Integer firstHourFee = Integer.parseInt(feeRule.getFirstHourFee().split(",")[weekday]);
            Integer otherFee = Integer.parseInt(feeRule.getOtherTimeFee().split(",")[weekday]);
            Integer maxFee = Integer.parseInt(feeRule.getMaxFee().split(",")[weekday]);
            Integer overTimeFee = Integer.parseInt(feeRule.getOverTimeFee().split(",")[weekday]);
            stopTime = stopTime - freeTime;
            if(stopTime>0){
                stopTime = (int) Math.ceil(stopTime/chargePeriod[0]);
                totalFee += firstHourFee;
                stopTime -= (int) (60/chargePeriod[0]);
            }
            if(stopTime>0){
                totalFee += stopTime*otherFee;
                if(totalFee>maxFee){
                    totalFee =  maxFee;
                }
            }
            if(over){
                Integer overTime = (int) Math.ceil((now-orderParking.getEndTime())/(1000.0*60*chargePeriod[0]));
                overFee = overTime*overTimeFee;
                orderParking.setOvertime(now-orderParking.getEndTime());
                orderParking.setOvertimeStr(TimeUtil.timeStampToHms(orderParking.getOvertime()));
            }
        }*/
        Long now = 0L;
        if(orderParking.getState().equals(Status.OrderParking.USED.getInt())){
            now = System.currentTimeMillis();
        }else {
            return;
        }
        Long endTimes = orderParking.getEndTime();
        Boolean over = now > endTimes;
        BigDecimal overFee = Constants.BIGDECIMAL_ZERO;
        if(over) {
//            // 区分周末时间
//            Calendar cal1 = Calendar.getInstance();
//            cal1.setTime(new Date(endTimes));
//            Calendar cal2 = Calendar.getInstance();
//            cal2.setTime(new Date(now));
//            boolean bool = isSameDay(cal1, cal2);
//            if (!bool) {
//                int days = cal1.get(Calendar.DATE);
//                int today = cal2.get(Calendar.DATE);
//                // 判断两个时间是否为同一天
//                if (today > days) {
//                    try {
//                        int workMinute = 0; // 工作日-分钟数
//                        int weekMinute = 0; // 周末-分钟数
//                        long min = 0; // 分钟数
//                        // 计算时间相差的天数
//                        int diff = differentDaysByMillisecond(cal1.getTime(), cal2.getTime());
//                        Calendar itemCal = Calendar.getInstance();
//                        itemCal.setTime(cal1.getTime());
//                        for (int i = 0; i <= diff; i++) {
//                            itemCal.add(Calendar.DATE, 1);
//                            int itemDay = itemCal.get(Calendar.DATE);
//                            if (itemDay != today) {
//                                int a = dayForWeek(itemCal);
//                                if (a < 6) {
//                                    workMinute += 24 * 60;  // 如果是工作日，累计一天
//                                } else {
//                                    weekMinute += 24 * 60;  // 如果是周末，累计一天
//                                }
//                            } else {
//                                // 如果在同一天，先初始
//                                Calendar initialCal = Calendar.getInstance();
//                                initialCal.setTime(itemCal.getTime());
//                                initialCal.set(Calendar.HOUR_OF_DAY, 0);
//                                initialCal.set(Calendar.MINUTE, 0);
//                                initialCal.set(Calendar.SECOND, 0);
//                                initialCal.set(Calendar.MILLISECOND, 0);
//
//                                long nm = 1000 * 60;
//
//                                long minute = cal2.getTimeInMillis() - initialCal.getTimeInMillis();
//                                System.out.println(cal2.getTimeInMillis());
//                                System.out.println(initialCal.getTimeInMillis());
//
//                                min = minute / nm;   //计算差多少分钟
//
//                                int a = dayForWeek(initialCal);
//                                if (a < 6) {
//                                    workMinute += min;  // 如果是工作日，累计一天
//                                } else {
//                                    weekMinute += min;  // 如果是周末，累计一天
//                                }
//                            }
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }


            List<ParklotParamsB> parklotTimeParamsList = parklotParamsMapper.queryParklotParams(orderParking.getParklotId(), Status.Common.VALID.getInt(), Constants.TIME_CODE);
            final Integer[] chargePeriod = {60};
            parklotTimeParamsList.stream().filter(param -> "min_charging_period".equalsIgnoreCase(param.getQhKey())).findAny().ifPresent(parklotParamsB -> chargePeriod[0] = Integer.parseInt(parklotParamsB.getQhValue()));
            ParkingFeeRuleInfoData infoData = feeRuleMapper.queryParkingFeeRuleById(orderParking.getParklotId());
            Integer type = infoData.getType();
            List<FeeRuleParking> feeRuleParkings = infoData.getFeeRuleParkings();
            BigDecimal overTimeFee = null;
            Integer dayOfWeek = 2;
            //如果规则分为工作日/周末
            if (1 == type) {
                dayOfWeek = TimeUtil.isWeekDay(now) ? 1 : 0;

            }
            if (null != feeRuleParkings) {
                for (FeeRuleParking feeRuleParking : feeRuleParkings) {
                    Integer weekEnd = feeRuleParking.getWeekday();
                    if (!dayOfWeek.equals(weekEnd)) {
                        continue;
                    }
                    Long startTime = feeRuleParking.getStartTime();
                    Long endTime = feeRuleParking.getEndTime();
                    //如果startTime为NULL，为24小时的计费规则，该计费规则按照排序排在最后  说明其他时间段都不符合，使用该规则
                    if (null == startTime) {
                        overTimeFee = feeRuleParking.getOverTimeFee();
                        break;
                    }
                    //把三个时间放到同一天,跨天就加一天
                    Long nowOneDay = TimeUtil.formatOneDayTime(now);
                    Long startTimeOneDay = TimeUtil.formatOneDayTime(startTime);
                    Long endTimeOneDay = TimeUtil.formatOneDayTime(endTime);
                    if (startTimeOneDay >= endTimeOneDay) {
                        endTimeOneDay = endTimeOneDay + Constants.TIMESTAMP_ONE_DAY;
                        endTime = endTime + Constants.TIMESTAMP_ONE_DAY;
                    }
                    //判断时间段是否符合
                    if (!(nowOneDay < endTimeOneDay && nowOneDay >= startTimeOneDay)) {
                        continue;
                    }
                    //判断时间区间是否符合
                    if (!(now < endTime && now > startTime)) {
                        continue;
                    }

                    overTimeFee = feeRuleParking.getOverTimeFee();
                    break;
                }
            }
            overTimeFee = (overTimeFee == null) ? Constants.BIGDECIMAL_ZERO : overTimeFee;
            Integer overTime = (int) Math.ceil((now - orderParking.getEndTime()) / (1000.0 * 60 * chargePeriod[0]));
            overFee = BigDecimal.valueOf(overTime).multiply(overTimeFee);
            orderParking.setOvertime(now - orderParking.getEndTime());
            orderParking.setOvertimeStr(TimeUtil.timeStampToHms(orderParking.getOvertime()));
        }
        orderParking.setOvertimeFee(overFee);
//        Long realEndTime = System.currentTimeMillis();
//        this.calculateParkingTotalFee(realEndTime,orderParking);
//        orderParking.setParkingFee(BigDecimal.valueOf(totalFee));
//        orderParking.setTotalFee(orderParking.getTotalFee().add(BigDecimal.valueOf(totalFee)));
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
//            while (data.getTimePointer() < realEndTime) {
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
//                if(i == (feeRuleParkingSize-1) && !matchFlag){
                    //如果没有匹配到规则，直接退出while循环
//                    break;
//                }
//            }
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
}

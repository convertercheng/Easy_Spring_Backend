package com.qhieco.apiservice.impl;

import com.qhieco.apiservice.LockService;
import com.qhieco.apiservice.ParklotService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.commonentity.*;
import com.qhieco.commonentity.relational.ParklotUsualB;
import com.qhieco.commonrepo.*;
import com.qhieco.config.ConfigurationFiles;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.map.Coordinate;
import com.qhieco.mapper.*;
import com.qhieco.request.api.ParklotDetailRequest;
import com.qhieco.request.api.ParklotNearByRequest;
import com.qhieco.request.api.ParklotUsualSetRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.api.*;
import com.qhieco.response.data.api.DiscountPackageData;
import com.qhieco.response.data.web.ParkingFeeRuleInfoData;
import com.qhieco.util.CommonUtil;
import com.qhieco.util.CoordinateUtil;
import com.qhieco.util.RespUtil;
import com.qhieco.util.TimeUtil;
import com.qhieco.mapper.DiscountPackageMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/2/24 13:57
 * <p>
 * 类说明：
 * 停车场service实现
 */
@Service
@Slf4j
public class ParklotServiceImpl implements ParklotService {

    @Autowired
    private ParklotMapper parklotMapper;

    @Autowired
    private ParklotRepository parklotRepository;

    @Autowired
    private ParklotUsualBRepository parklotUsualBRepository;

    @Autowired
    private UserMobileRepository userMobileRepository;

    @Autowired
    private ParklotAmountRepository parklotAmountRepository;

    @Autowired
    private ShareMapper shareMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ParklotParamsBRepository parklotParamsBRepository;

    @Autowired
    private OrderParkingRepository orderParkingRepository;

    @Autowired
    private PlateMapper plateMapper;

    @Autowired
    private IntegralPermissionsLevelRepository integralPermissionsLevelRepository;

    @Autowired
    private ConfigurationFiles configurationFiles;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private FeeRuleMapper feeRuleMapper;

    @Autowired
    private ParklocRepository parklocRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private LockService lockService;

    @Autowired
    private DiscountPackageMapper discountPackageMapper;

    private static final int DELETE_USUAL_PARKLOT = -1;

    @Override
    public ParklotUsualRepData queryParklotUsual(Integer userId) {
        return parklotMapper.queryParklotUsualByUserId(userId);
    }

    @Override
    public ParklotListRepData queryParklotListByKeywords(Double x, Double y, String keywords, Integer pageSize, Integer pageNum) {
        ParklotListRepData parklotListRepData = new ParklotListRepData();
        HashMap<String, Object> paramMap = new HashMap<>(8);
        paramMap.put("x", x);
        paramMap.put("y", y);
        paramMap.put("keywords", keywords);

        if(pageNum != null && pageSize != null && pageSize > 0) {
            int startPage = pageNum <= 0 ? 0 : pageNum * pageSize;
            paramMap.put("startPage", startPage);
            paramMap.put("pageSize", pageSize);
        }
        log.info("停车场查询参数：" + paramMap);
        parklotListRepData.setParklots(parklotMapper.queryParklotListByKeywords(paramMap));
        return parklotListRepData;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Resp setUsual(ParklotUsualSetRequest parklotUsualSetRequest) {
        //验证时间戳是否合法
        if (CommonUtil.isTimeStampInValid(parklotUsualSetRequest.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        //验证停车场是否存在
        final Integer parklotId = parklotUsualSetRequest.getParklot_id();
        if(DELETE_USUAL_PARKLOT != parklotId && null == parklotRepository.findOne(parklotId)){
            throw new QhieException(Status.ApiErr.NONEXISTENT_PARKLOT);
        }
        //验证手机用户是否存在
        final Integer userId = parklotUsualSetRequest.getUser_id();
        if(null == userMobileRepository.findOne(userId)){
            throw new QhieException(Status.ApiErr.NONEXISTENT_USER);
        }
        //如果传入停车场Id为-1，删除常用停车场
        if(DELETE_USUAL_PARKLOT == parklotId){
            parklotUsualBRepository.deleteParklotUsual(userId,Status.Common.DELETED.getInt());
            return RespUtil.successResp();
        }
        //查询该用户是否存在常用停车场，如果存在修改常用停车场
        List<ParklotUsualB> parklotUsualBs = parklotUsualBRepository.findByUserId(userId);
        if(null != parklotUsualBs && parklotUsualBs.size() > Constants.EMPTY_CAPACITY){
            parklotUsualBRepository.shift(userId, parklotId,Status.Common.VALID.getInt());
            return RespUtil.successResp();
        }
        //不是删除或者修改时，新增常用停车场
        ParklotUsualB parklotUsualB = new ParklotUsualB(userId, parklotId,System.currentTimeMillis(), Constants.PARKING_USUAL_FIFTH,Status.Common.VALID.getInt());
        parklotUsualBRepository.save(parklotUsualB);
        return RespUtil.successResp();
    }

    @Override
    public Resp detail(ParklotDetailRequest parklotDetailRequest) {
        //验证时间戳是否合法
        if (CommonUtil.isTimeStampInValid(parklotDetailRequest.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        Parklot parklot = parklotRepository.findOne(parklotDetailRequest.getParklot_id());
        if(null == parklot){
            throw new QhieException(Status.ApiErr.NONEXISTENT_PARKLOT);
        }
        ParklotDetailRepData parklotDetailRepData = new ParklotDetailRepData();
        ParklotAmount parklotAmount = parklotAmountRepository.findByParklotId(parklotDetailRequest.getParklot_id());
        List<File> files = fileMapper.queryFileListByParklotId(parklotDetailRequest.getParklot_id());
        //如果是停车场类型为约车位，查询可预约车位
        if(parklot.getType() == Constants.PARKING_SHARE) {
            // 查询车场所有的可预约时间段
            HashMap<String, Object> params = new HashMap<>(16);
            params.put("parklotId", parklotDetailRequest.getParklot_id());
            String advanceReservationTime = parklotParamsBRepository.findValueByParklotId(parklotDetailRequest.getParklot_id(), Constants.ADVANCE_RESERVATION_TIME, Status.Common.VALID.getInt());
//            String minSharePeriod = parklotParamsBRepository.findValueByParklotId(parklotDetailRequest.getParklot_id(), Constants.MIN_SHARING_PERIOD, Status.Common.VALID.getInt());
            // 业主发布的车位共享时间的最短时长
//            if (StringUtils.isNotEmpty(minSharePeriod)) {
//                params.put("timeInterval", Integer.valueOf(minSharePeriod) + TimeUtil.getTimeInterval());
//            } else {
//                params.put("timeInterval", Integer.valueOf(Constants.MIN_SHARING_PERIOD_DEFAULT) + TimeUtil.getTimeInterval());
//            }
            long timeInterval = (TimeUtil.getMinChargingPeriod() * 60 * 1000 + TimeUtil.getTimeInterval() * 60 * 1000);
            params.put("timeInterval", timeInterval);
            // 车位可以提前多长时间预约。缺省值120分钟
            if (StringUtils.isNotEmpty(advanceReservationTime)) {
                params.put("advanceReservationTime", Integer.valueOf(advanceReservationTime));
            } else {
                params.put("advanceReservationTime", Integer.valueOf(Constants.ADVANCE_RESERVATION_TIME_DEFAULT));
            }
            log.info("查询车场可预约时间段的参数，params = " + params);
            List<ParklocShareRepData> parklocShareRepDataList = shareMapper.queryReserveTimeListByParklotId(params);
            log.info("车位可预约时间段 parklocShareRepDataList=" + parklocShareRepDataList);
//            parklotDetailRepData.setParklocShareRepData(parklocShareRepDataList);
            List<ParklocShareRepData> list = new ArrayList<>();
            for (int i = 0; i < parklocShareRepDataList.size(); i++) {
                if (lockService.checkLockAvailable(parklocShareRepDataList.get(i).getParklocId())) {
                    list.add(parklocShareRepDataList.get(i));
                }
            }
            parklotDetailRepData.setParklocShareRepData(list);
        }
        //计算用户到停车场的距离
        if(null != parklotDetailRequest.getLng() && null != parklotDetailRequest.getLat()) {
            Coordinate start = new Coordinate();
            start.setLongitude(parklotDetailRequest.getLng());
            start.setLatitude(parklotDetailRequest.getLat());
            Coordinate end = new Coordinate();
            end.setLongitude(parklot.getLng());
            end.setLatitude(parklot.getLat());
            double distance = CoordinateUtil.getDistance(start, end);
            parklotDetailRepData.setDistance(getDistance((int) distance));
        }
        if (files != null && files.size() > 0) {
            for (File file : files) {
                if (StringUtils.isNotEmpty(file.getPath())) {
                    file.setPath(configurationFiles.getPicPath() + file.getPath());
                }
            }
        }
        DiscountPackageData discountPackageData=discountPackageMapper.findParklotPackageByParkId(parklot.getId());
        if(discountPackageData==null){
            discountPackageData=new DiscountPackageData();
            discountPackageData.setState(Constants.PACKAGE_CHARGE_TYPE_ZEOR);
        }
        parklotDetailRepData.setPackageState(discountPackageData.getState());
        parklotDetailRepData.setFiles(files);
        parklotDetailRepData.setParklotId(parklot.getId());
        parklotDetailRepData.setParklotName(parklot.getName());
        parklotDetailRepData.setParklotKind(parklot.getKind());
        parklotDetailRepData.setParklotAddress(parklot.getAddress());
        parklotDetailRepData.setParklotType(parklot.getType());
        parklotDetailRepData.setParklotLng(parklot.getLng());
        parklotDetailRepData.setParklotLat(parklot.getLat());
        parklotDetailRepData.setTotalAmount(parklotAmount.getTotalAmount());
        parklotDetailRepData.setIdleAmount(parklotAmount.getIdleAmount());
        parklotDetailRepData.setLeftAmount(parklotAmount.getLeftAmount());
        parklotDetailRepData.setLeftAmountType(parklotAmount.getLeftAmountType());
        parklotDetailRepData.setReservableAmount(parklotAmount.getReservableAmount());
        return RespUtil.successResp(parklotDetailRepData);
    }

    @Override
    public ParklotNearByRepData queryNearByParklot(ParklotNearByRequest parklotNearByRequest) {
        if (CommonUtil.isTimeStampInValid(parklotNearByRequest.getTimestamp())) {
            log.error(Status.ApiErr.TIMESTAMP_ERROR.getMsg());
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        Integer radius = parklotNearByRequest.getRadius();
        if (radius <= Constants.MIN_NON_NEGATIVE_INTEGER) {
            log.error(Status.ApiErr.NUMBER_RANGE_ERROR.getMsg());
            throw new QhieException(Status.ApiErr.NUMBER_RANGE_ERROR);
        }
        ParklotNearByRepData parklotNearByRepData = new ParklotNearByRepData();
        ParklotNearByRequest.MapBean map = parklotNearByRequest.getMap();
        ParklotNearByRequest.LocateBean locate = parklotNearByRequest.getLocate();
        List<Parklot> parklots = parklotMapper.queryParklotNearby(map.getLat(), map.getLng(), locate.getLat(), locate.getLng(), radius);
        List<ParklotNearByRepData.ParklotsBean> parklotsBeans = new ArrayList<>();
        ParklotNearByRepData.ParklotsBean parklotsBean;
        for (Parklot parklot: parklots) {
            parklotsBean = new ParklotNearByRepData.ParklotsBean();
            DiscountPackageData discountPackageData=discountPackageMapper.findParklotPackageByParkId(parklot.getId());
            if(discountPackageData==null){
                discountPackageData=new DiscountPackageData();
                discountPackageData.setState(Constants.PACKAGE_CHARGE_TYPE_ZEOR);
            }
            parklotsBean.setPackageState(discountPackageData.getState());
            setParklotBaseInformation(parklotsBean, parklot);
            setParklotLatLngInfo(parklotsBean, parklot);
            parklotsBean.setDistance(getDistance(parklot.getLocateDistance()));
            int parklotId = parklot.getId();
            setParklotAmountInfo(parklotsBean, parklotId);
            setParklotFeeRuleInfo(parklotsBean, parklotId);
            parklotsBeans.add(parklotsBean);
        }
        parklotNearByRepData.setParklots(parklotsBeans);
        return parklotNearByRepData;
    }

    /**
     * 设置停车场的收费规则信息
     */
    private void setParklotFeeRuleInfo(ParklotNearByRepData.ParklotsBean parklotsBean, int parklotId) {
        String feeRuleReserve = parklotMapper.queryParklotReserveFeeRule(parklotId);
        parklotsBean.setReserve_fee(new BigDecimal(StringUtils.isEmpty(feeRuleReserve) ? "0.00" :
                feeRuleReserve.split(Constants.DELIMITER_COMMA)[0]));
        //筛选停车费
        ParkingFeeRuleInfoData infoData = feeRuleMapper.queryParkingFeeRuleById(parklotId);
        if (infoData == null) {
            log.error("车场的费用规则为空， parklotId = " + parklotId);
            return;
        }
        List<FeeRuleParking> feeRuleParkings = infoData.getFeeRuleParkings();
        Integer type = infoData.getType();
        Integer dayOfWeek = 2;
        BigDecimal parkinFee = Constants.BIGDECIMAL_ZERO;
        Long now = System.currentTimeMillis();
        //如果规则分为工作日/周末
        if (1 == type) {
            dayOfWeek = TimeUtil.isWeekDay(System.currentTimeMillis()) ? 1 : 0;
        }
        if (null != feeRuleParkings) {
            for (FeeRuleParking feeRuleParking : feeRuleParkings) {
                Long startTime = feeRuleParking.getStartTime();
                Long endTime = feeRuleParking.getEndTime();
                Integer weekEnd = feeRuleParking.getWeekday();
                if (!dayOfWeek.equals(weekEnd)) {
                    continue;
                }
                //如果startTime为NULL，为24小时的计费规则，该计费规则按照排序排在最后  说明其他时间段都不符合，使用该规则
                if (null == startTime) {
                    parkinFee = feeRuleParking.getFirstHourFee();
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
                parkinFee = feeRuleParking.getFirstHourFee();
                break;
            }
        }
        parklotsBean.setParking_fee(parkinFee);
    }

    /**
     * 设置停车场的车位数量信息
     */
    private void setParklotAmountInfo(ParklotNearByRepData.ParklotsBean parklotsBean, int parklotId) {
        try {
            ParklotAmount parklotAmount = parklotAmountRepository.findByParklotId(parklotId);
            parklotsBean.setTotal_amount(null == parklotAmount ? Constants.MIN_NON_NEGATIVE_INTEGER : parklotAmount.getTotalAmount());
            parklotsBean.setReservable_amount(null == parklotAmount ? Constants.MIN_NON_NEGATIVE_INTEGER : parklotAmount.getReservableAmount());
            parklotsBean.setLeft_amount(null == parklotAmount ? Constants.MIN_NON_NEGATIVE_INTEGER : parklotAmount.getLeftAmount());
            parklotsBean.setShow(null == parklotAmount ? Status.ParklocNum.HIDDEN.getInt() : parklotAmount.getLeftAmountType());
        } catch (Exception e) {
            log.error("设置停车场的车位数量信息异常，" + e);
        }
    }

    /**
     * 设置停车场的经纬度信息
     */
    private void setParklotLatLngInfo(ParklotNearByRepData.ParklotsBean parklotsBean, Parklot parklot) {
        parklotsBean.setLat(String.valueOf(parklot.getLat()));
        parklotsBean.setLng(String.valueOf(parklot.getLng()));
        parklotsBean.setNavi_lat(String.valueOf(parklot.getNaviLat()));
        parklotsBean.setNavi_lng(String.valueOf(parklot.getNaviLng()));
    }

    /**
     * 设置停车场的基本信息
     */
    private void setParklotBaseInformation(ParklotNearByRepData.ParklotsBean parklotsBean, Parklot parklot) {
        parklotsBean.setId(parklot.getId());
        parklotsBean.setName(parklot.getName());
        parklotsBean.setType(parklot.getType());
        parklotsBean.setAddress(parklot.getAddress());
        parklotsBean.setKind(parklot.getKind());
        parklotsBean.setCharge_type(parklot.getChargeType());
    }

    /**
     * 返回距离字符串
     * 距离未超1000米时，长度单位显示为m。距离超过1000米时，长度单位显示为km，精确到小数点后1位
     * @param distance 距离
     * @return 距离字符串
     */
    private String getDistance(Integer distance) {
        if (null == distance || distance <= Constants.MIN_NON_NEGATIVE_INTEGER) {
            return "距离很近";
        }
        if (distance < Constants.ONE_KILOMETER) {
            return String.format("%sm", distance);
        } else {
            return String.format("%.1fkm", distance / 1000.0);
        }
    }

    @Override
    public ReserveEnterRepData queryReserveInfo(Integer parklotId, Integer userId,Integer parklocId, Integer parklotDistrictId) {
        ReserveEnterRepData reserveEnterRepData = new ReserveEnterRepData();
        if (parklocId != null && parklotId == null) {
            Parkloc parkloc = parklocRepository.findOne(parklocId);
            if (null != parkloc) {
                reserveEnterRepData.setParklocNumber(parkloc.getNumber());
                parklotId = parkloc.getParklotId();
            }
        } else if (parklotDistrictId != null && parklotId == null) {
            Parkloc parkloc = parklocRepository.findByParklotDistrictId(parklotDistrictId);
            if (null != parkloc) {
                reserveEnterRepData.setParklocNumber(parkloc.getNumber());
                parklotId = parkloc.getParklotId();
            }
        }
        setBaseInformation(parklotId, reserveEnterRepData);
        // userID不为空，则查询车牌号
        if (userId != null) {
            setPlateInfo(userId, reserveEnterRepData);
        }
        Integer advanceReservationTime = parklotMapper.queryParklotParamsValueByParklotId(parklotId, Status.Common.VALID.getInt(), Constants.ADVANCE_RESERVATION_TIME);
        setTimeParams(parklotId, reserveEnterRepData, advanceReservationTime);
        setReserveFee(parklotId, reserveEnterRepData);
        HashMap<String, Object> params = new HashMap<>(8);
        params.put("parklotId", parklotId);
        long timeInterval = (TimeUtil.getMinChargingPeriod() * 60 * 1000 + TimeUtil.getTimeInterval() * 60 * 1000);
        params.put("timeInterval", timeInterval);
        // 车位可以提前多长时间预约。缺省值120分钟
        if (advanceReservationTime != null) {
            params.put("advanceReservationTime", advanceReservationTime);
        } else {
            params.put("advanceReservationTime", Integer.valueOf(Constants.ADVANCE_RESERVATION_TIME_DEFAULT));
        }
        UserMobile userMobile = null;
        if(null != userId) {
            userMobile = userMobileRepository.findOne(userId);
        }
        Double integralPermissionsCoefficient = null;
        if(null != userMobile){
            integralPermissionsCoefficient = integralPermissionsLevelRepository.queryReserveCoefficientByIntegral(userMobile.getIntegral());

        }
        if(null == integralPermissionsCoefficient){
            integralPermissionsCoefficient = 1d;
        }
        reserveEnterRepData.setIntegralPermissionsCoefficient(integralPermissionsCoefficient);
        List<ReserveTimeData> reserveTimeDataList;
        if (null != parklocId) {
            params.put("parklocId", parklocId);
            reserveTimeDataList = shareMapper.queryReserveTimeListByParklocIdAndCondition(params);
            if (reserveTimeDataList == null || reserveTimeDataList.size() == 0) {
                log.error("当前车位不可预约");
                throw new QhieException(Status.ApiErr.EMPTY_RESERVE_PARKLOC);
            }

        } else if (parklotDistrictId != null) {
            params.put("districtId", parklotDistrictId);
            reserveTimeDataList = shareMapper.queryReserveTimeListByDistrictId(params);

            if (reserveTimeDataList == null || reserveTimeDataList.size() == 0) {
                log.error("当前区域没有可预约的车位");
                throw new QhieException(Status.ApiErr.EMPTY_DISTINCT_PARKLOC);
            }

        } else {
            reserveTimeDataList = shareMapper.queryReserveTimeListByCondition(params);
        }
        List<ReserveTimeData> list = new ArrayList<>();
        for (int i = 0; i < reserveTimeDataList.size(); i++) {
            if (lockService.checkLockAvailable(reserveTimeDataList.get(i).getParklocId())) {
                list.add(reserveTimeDataList.get(i));
            }
        }
        reserveEnterRepData.setReserveTimeList(list);
        // 返回系统时间
        reserveEnterRepData.setSystemTime(System.currentTimeMillis());

        // 查询用户是否存在未完成的订单
        HashMap<String, Object> param = new HashMap<>(8);
        param.put("userId", userId);
        param.put("unconfirmed", Status.OrderParking.UNCONFIRMED.getInt());
        param.put("reserved", Status.OrderParking.RESERVED.getInt());
        param.put("used", Status.OrderParking.USED.getInt());
        param.put("unpaid", Status.OrderParking.UNPAID.getInt());
        OrderUsingRepData orderUsingRepData = orderMapper.queryOrderUsing(param);
        if (orderUsingRepData != null) {
            reserveEnterRepData.setOrderId(orderUsingRepData.getOrderId());
        }
        return reserveEnterRepData;
    }

    /**
     * 设置预约费用规则列表
     * @param parklotId 停车区id
     * @param reserveEnterRepData 返回数据
     */
    private void setReserveFee(Integer parklotId, ReserveEnterRepData reserveEnterRepData) {
        FeeRuleReserve feeRuleReserve = parklotMapper.parklotReserveFeeRule(parklotId);
        List<ReserveFeeData> feeList = new ArrayList<>();
        if (null != feeRuleReserve) {
            String[] fees = feeRuleReserve.getFee().split(Constants.DELIMITER_COMMA);
            String[] finishTimes = feeRuleReserve.getFinishTime().split(Constants.DELIMITER_COMMA);
            ReserveFeeData reserveFeeData;
            for (int i = 0; i < fees.length; i++) {
                reserveFeeData = new ReserveFeeData(i, Integer.valueOf(finishTimes[i]), new BigDecimal(fees[i]));
                feeList.add(reserveFeeData);
            }
        }
        reserveEnterRepData.setFeeList(feeList);
    }

    /**
     * 设置时间参数相关
     * @param parklotId 停车区id
     * @param reserveEnterRepData 返回数据
     */
    private void setTimeParams(Integer parklotId, ReserveEnterRepData reserveEnterRepData, Integer advanceReservationTime) {
        String freeCancellationTime = parklotParamsBRepository.findValueByParklotId(parklotId, Constants.FREE_CANCELLATION_TIME, Status.Common.VALID.getInt());
        String maxDelayTime = parklotParamsBRepository.findValueByParklotId(parklotId, Constants.MAX_DELAY_TIME, Status.Common.VALID.getInt());
        reserveEnterRepData.setFreeCancellationTime(Integer.valueOf(StringUtils.isEmpty(freeCancellationTime)? Constants.FREE_CANCELLATION_TIME_DEFAULT: freeCancellationTime));
        reserveEnterRepData.setMaxDelayTime(Integer.valueOf(StringUtils.isEmpty(maxDelayTime)? Constants.MAX_DELAY_TIME_DEFAULT: maxDelayTime));
        reserveEnterRepData.setAdvanceReservationTime(advanceReservationTime == null? Integer.valueOf(Constants.ADVANCE_RESERVATION_TIME_DEFAULT): advanceReservationTime);
        // 车位可以预约到的最长时间点
        Long advanceReservationTimePoint = System.currentTimeMillis() + TimeUtil.minutesToMilliSeconds(reserveEnterRepData.getAdvanceReservationTime());
        reserveEnterRepData.setAdvanceReservationTimePoint(advanceReservationTimePoint);
    }

    /**
     * 设置车牌信息
     * @param userId 用户id
     * @param reserveEnterRepData 返回数据
     */
    private void setPlateInfo(Integer userId, ReserveEnterRepData reserveEnterRepData) {
        OrderParking orderParking = orderParkingRepository.findTopByMobileUserIdOrderByIdDesc(userId);
        Integer plateId ;
        String plateNo ;
        if (null == orderParking) {
            // 没有下过单，则查询最后添加的车牌号
            Plate plate = userMapper.queryPlateByUserId(userId);
            plateId = null == plate ? Constants.EMPTY_CAPACITY : plate.getId();
            plateNo = null == plate ? Constants.EMPTY_STRING : plate.getNumber();
        } else {
            // 下过单，查询最后一次下单的车牌号
            HashMap plateMap = plateMapper.queryPlateInfoByPlateIdAdUserId(orderParking.getPlateId(), orderParking.getMobileUserId(),
                    Status.Common.VALID.getInt());
            if (plateMap != null && plateMap.containsKey("plateNo") && plateMap.containsKey("plateId")) {
                plateId = Integer.valueOf(plateMap.get("plateId").toString());
                plateNo = plateMap.get("plateNo").toString();
            } else {
                // 如果最后一次下单的车牌号不可用，查询最后添加的车牌号
                Plate plate = userMapper.queryPlateByUserId(userId);
                plateId = null == plate ? Constants.EMPTY_CAPACITY : plate.getId();
                plateNo = null == plate ? Constants.EMPTY_STRING : plate.getNumber();
            }
        }
        reserveEnterRepData.setPlateId(plateId);
        reserveEnterRepData.setPlateNo(plateNo);
    }

    /**
     * 设置基本信息
     * @param parklotId 停车区id
     * @param reserveEnterRepData 返回数据
     */
    private void setBaseInformation(Integer parklotId, ReserveEnterRepData reserveEnterRepData) {
        reserveEnterRepData.setParklotId(parklotId);
        Parklot parklot = parklotRepository.findOne(parklotId);
        reserveEnterRepData.setParklotName(parklot.getName());
        reserveEnterRepData.setParklotKind(parklot.getKind());
        reserveEnterRepData.setAddress(parklot.getAddress());
        reserveEnterRepData.setFeeIntro(parklot.getFeeIntro());
        reserveEnterRepData.setType(parklot.getType());
        Integer reservableAmount = parklotAmountRepository.findReservableAmountByParklotId(parklotId);
        reserveEnterRepData.setReservableAmount(reservableAmount);
    }

    @Override
    public MinPublishIntervalRespData queryMinPublishIntervalByUserId(Integer userId, Integer parklotId) {
        String minSharePeriod;
        if (null == parklotId || 0 == parklotId) {
            List<Integer> parklotIds = parklotRepository.findIdsByMobileUserId(userId);
            if (null == parklotIds || Constants.EMPTY_CAPACITY == parklotIds.size()) {
                throw new QhieException(Status.ApiErr.NONEXISTENT_PARKLOT);
            }
            if (Constants.ONE_CAPACITY < parklotIds.size()) {
                throw new QhieException(Status.ApiErr.NOT_SUPPORT_ONE_ADMIN_TO_MANY_PARKLOTS);
            }
            parklotId = parklotIds.get(Constants.FIRST_INDEX);
        }
        minSharePeriod = parklotMapper.queryMinPublishTimeInterval(parklotId, Constants.MIN_SHARING_PERIOD);
        MinPublishIntervalRespData minPublishIntervalRespData = new MinPublishIntervalRespData();
        minPublishIntervalRespData.setMinSharePeriod(minSharePeriod);
        return minPublishIntervalRespData;
    }
}

package com.qhieco.apiservice.impl;

import com.qhieco.apiservice.ParklotAmountService;
import com.qhieco.apiservice.PublishService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.commonentity.OrderParking;
import com.qhieco.commonentity.Parkloc;
import com.qhieco.commonentity.Publish;
import com.qhieco.commonentity.Share;
import com.qhieco.commonrepo.*;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.mapper.ParklocMapper;
import com.qhieco.mapper.ParklotMapper;
import com.qhieco.mapper.PublishMapper;
import com.qhieco.request.api.PublishAddRequest;
import com.qhieco.request.api.PublishBatchAddRequest;
import com.qhieco.request.api.PublishCancelRequest;
import com.qhieco.request.api.PublishListAlterRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.api.ParklotIdAdParklocIdData;
import com.qhieco.response.data.api.PublishFaiNumberRepData;
import com.qhieco.time.TimePeriod;
import com.qhieco.util.CommonUtil;
import com.qhieco.util.ParamUtil;
import com.qhieco.util.RespUtil;
import com.qhieco.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/7 13:57
 * <p>
 * 类说明：
 * 发布service实现
 */
@Service
@Slf4j
public class PublishServiceImpl implements PublishService {

    @Autowired
    PublishRepository publishRepository;

    @Autowired
    ShareRepository shareRepository;

    @Autowired
    UserMobileRepository userMobileRepository;

    @Autowired
    ParklocRepository parklocRepository;

    @Autowired
    ParklotParamsBRepository parklotParamsBRepository;

    @Autowired
    ParklotMapper parklotMapper;

    @Autowired
    ParklocMapper parklocMapper;

    @Autowired
    ParklotAmountRepository parklotAmountRepository;

    @Autowired
    private ParklotAmountService parklotAmountService;

    @Autowired
    OrderParkingRepository orderParkingRepository;

    @Autowired
    private PublishMapper publishMapper;

    @PersistenceContext
    EntityManager em;

    @Override
    @Transactional
    public Resp addPublish(PublishAddRequest publishAddRequest){
        //验证时间戳是否合法
        if (CommonUtil.isTimeStampInValid(publishAddRequest.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        Long now = System.currentTimeMillis();
        Integer parklocId = publishAddRequest.getParkloc_id();
        Integer userId = publishAddRequest.getUser_id();
        Integer mode = publishAddRequest.getMode();
        Integer parklotId = publishAddRequest.getParklot_id();
        // dayOfWeek客户端传来的是0～6
        String dayOfWeek = publishAddRequest.getDay_of_week();
        if (ParamUtil.isInValidDayOfWeeks(mode, dayOfWeek)) {
            throw new QhieException(Status.ApiErr.PARAMS_PUBLISH_ADD);
        }
        Long startTime = publishAddRequest.getStart_time();
        Long endTime = publishAddRequest.getEnd_time();
        //校验时间的合法性
        if (!checkTime(parklotId, startTime, endTime)) {
            throw new QhieException(Status.ApiErr.PUBLISH_TIME_ILLEGAL);
        }
        //验证停车位和用户是否对应存在
        if(null == parklocRepository.findByIdAndMobileUserIdAndState(parklocId,userId,Status.Common.DELETED.getInt())){
            log.error("parklocId:" + parklocId + " result: " + Status.ApiErr.NONEXISTENT_PARKLOC_USER.getMsg());
            throw new QhieException(Status.ApiErr.NONEXISTENT_PARKLOC_USER);
        }
        //校验发布时间是否重复
        List<Publish> publishList = parklocMapper.findValidPublishList(parklocId);
        if (isPublishTimeRepetition(publishList, parklotId, startTime, endTime, dayOfWeek, mode)) {
            log.error("parklocId: " + parklocId + " result: " + Status.ApiErr.REPEAT_PUBLISH_TIME.getMsg());
            throw new QhieException(Status.ApiErr.REPEAT_PUBLISH_TIME);
        }
        Publish publish = new Publish(parklocId,startTime,endTime, TimeUtil.makeTimeRange(startTime, endTime), mode, dayOfWeek, Status.Common.VALID.getInt());
        //保存到发布表
        Publish resPublish;
        if(null == (resPublish = publishRepository.save(publish))){
            log.error("parklocId:"+parklocId +" result:"+Status.ApiErr.INSERT_ERROR.getMsg());
            throw new QhieException(Status.ApiErr.INSERT_ERROR);
        }
        //修改车位状态为已发布
        parklocRepository.updateStateByParklocId(parklocId,Status.Parkloc.PUBLISHED.getInt());
        int todayOfWeek = TimeUtil.getDayOfWeekToday();
        //如果不是重复发布直接插入到Share表
        this.share(resPublish.getMode(),parklocId,resPublish.getId(),resPublish.getStartTime(),resPublish.getEndTime(),resPublish.getDayOfWeek(),todayOfWeek);

        //  更新该车场的parklot_amount表的数据
        parklotAmountService.updateParklotAmountInfoByParklotId(parklotId, "addPublish 发布车位");
        return RespUtil.successResp();
    }

    /**
     * 校验发布时间是否合法
     * @param reqStartTime 开始时间
     * @param parklotId 停车区id
     * @param reqEndTime 结束时间
     * @return 检测发布时间是否合法
     */
    private Boolean checkTime(Integer parklotId,Long reqStartTime, Long reqEndTime) {
        String value = parklotParamsBRepository.findValueByParklotId(parklotId,Constants.MIN_SHARING_PERIOD,Status.Common.VALID.getInt());
        if(StringUtils.isEmpty(value)){
            value = Constants.MIN_SHARING_PERIOD_DEFAULT;
        }
        long minSharingPeriod = TimeUtil.minutesToMilliSeconds(Integer.valueOf(value));
        // 开始时间和结束时间差 小于最小共享时长，非法
        if (reqEndTime - reqStartTime < minSharingPeriod) {
            log.error("发布数据非法  sharing period less than minSharingPeriod, minSharingPeriod: {}, reqStartTime: {}, reqEndTime: {}",
                    minSharingPeriod, reqStartTime, reqEndTime);
            return false;
        }
        return true;
    }

    /**
     * 检验发布时间是否与已发布的重复
     * @param publishList 发布的车位列表
     * @return 与该车位已发布的时间段重复
     */
    private boolean isPublishTimeRepetition(List<Publish> publishList, Integer parklotId, Long startTime, Long endTime, String dayOfWeeks, Integer mode) {
        boolean result = false;
        /*
         * 1 获取当前车位每天的发布时间段情况
         */
        String value = getMinPublishInterval(parklotId);
        HashMap<Integer, ArrayList<TimePeriod>> publishPeriod = getPublishInfoOneWeek(publishList, startTime, Integer.valueOf(value) * TimeUtil.MINUTE_2_MSECONDS);
        /*
         * 2 判断是否重复
         */
        String timeRange = TimeUtil.makeTimeRange(startTime, endTime);
        String[] timeRanges = timeRange.split(Constants.DELIMITER_POUND);
        TimePeriod firstPeriod = new TimePeriod(timeRanges[0]);
        log.info("first timePeriod is {}", firstPeriod);
        if (Status.PublishMode.ONCE.getInt().equals(mode)) {
            /*
             * 验证第一段时间
             */
            int today = TimeUtil.dayOfWeek(startTime);
            result = TimeUtil.isTimePeriodInList(firstPeriod, publishPeriod.get(today), value);
            if (result) {
                return true;
            }
            log.info("单次发布，验证过了第一段时间");
            /*
             * 如果有，验证第二段时间
             */
            if (timeRanges.length > 1) {
                TimePeriod secondPeriod = new TimePeriod(timeRanges[1]);
                log.info("second timePeriod is {}", secondPeriod);
                result = TimeUtil.isTimePeriodInList(secondPeriod, publishPeriod.get((today + 1) % 7), value);
            }
        } else if (Status.PublishMode.LOOP.getInt().equals(mode)){
            /*
             * 验证第一段时间
             */
            for (String day: dayOfWeeks.split(Constants.DELIMITER_COMMA)) {
                result = TimeUtil.isTimePeriodInList(firstPeriod, publishPeriod.get(Integer.valueOf(day)), value);
                if (result) {
                    return true;
                }
            }
            log.info("循环发布，验证过了第一段时间");
            /*
             * 如果有，验证第二段时间
             */
            if (timeRanges.length > 1) {
                TimePeriod secondPeriod = new TimePeriod(timeRanges[1]);
                log.info("second timePeriod is {}", secondPeriod);
                for (String day: dayOfWeeks.split(Constants.DELIMITER_COMMA)) {
                    result = TimeUtil.isTimePeriodInList(secondPeriod, publishPeriod.get((Integer.valueOf(day) + 1) % 7), value);
                }
            }
        }
        return result;
    }

    /**
     * 获取最小发布时间间隔
     * @param parklotId 停车场id
     * @return 发布时间最短间隔
     */
    private String getMinPublishInterval(Integer parklotId) {
        String value = parklotMapper.queryMinPublishTimeInterval(parklotId, Constants.MIN_PUBLISH_INTERVAL);
        log.info("min publish interval is {}", value);
        if(StringUtils.isEmpty(value)){
            value = Constants.MIN_PUBLISH_INTERVAL_DEFAULT;
        }
        return value;
    }

    /**
     * 根据查询出来的发布信息构建一个映射表， 星期几 ---> list { time period 1, time period 2, time period 3 }
     * @param publishList 发布信息list
     * @return HashMap
     */
    private HashMap<Integer, ArrayList<TimePeriod>> getPublishInfoOneWeek(List<Publish> publishList, Long startTime, Long minPublishInterval) {
        HashMap<Integer, ArrayList<TimePeriod>> publishPeriod = new HashMap<>(225);
        Integer[] weeks = {0, 1, 2, 3, 4, 5, 6};
        for (Integer day: weeks) {
            publishPeriod.put(day, new ArrayList<>());
        }
        for (Publish publish: publishList) {
            this.createPublishTime(publishPeriod,publish.getTimeRange(),publish.getMode(),publish.getDayOfWeek(),minPublishInterval,publish.getStartTime(),publish.getEndTime());
            //如果发布是待修改状态，把上一次的发布时间段拿出来比较
            Integer publishState = publish.getState();
            if(Status.Publish.TOBEALTER.getInt().equals(publishState)){
                this.createPublishTime(publishPeriod,publish.getLastTimeRange(),publish.getLastMode(),publish.getLastDayOfWeek(),minPublishInterval,publish.getLastStartTime(),publish.getLastEndTime());
            }
        }
        log.info("------------- publishPeriod ---- start ---------");
        for (Integer day: weeks) {
            log.info("{}: {}", day, publishPeriod.get(day));
        }
        log.info("------------- publishPeriod ----- end --------");
        return publishPeriod;
    }

    private void createPublishTime(HashMap<Integer, ArrayList<TimePeriod>> publishPeriod,String publishTimeRange,Integer mode,String dayOfWeek,Long minPublishInterval,Long startTime,Long endTime){
        // 循环发布
        String[] timeRange = publishTimeRange.split(Constants.DELIMITER_POUND);
        if (Status.PublishMode.LOOP.getInt().equals(mode)) {
            for (String day: dayOfWeek.split(Constants.DELIMITER_COMMA)) {
                publishPeriod.get(Integer.valueOf(day)).add(new TimePeriod(timeRange[0]));
                if (timeRange.length > 1) {
                    publishPeriod.get((Integer.valueOf(day) + 1) % 7).add(new TimePeriod(timeRange[1]));
                }
            }
        } else {
            // 一次性发布
            if (endTime + minPublishInterval > startTime) {
                int day = TimeUtil.dayOfWeek(startTime);
                publishPeriod.get(day).add(new TimePeriod(timeRange[0]));
                if (timeRange.length > 1) {
                    publishPeriod.get((day + 1) % 7).add(new TimePeriod(timeRange[1]));
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp cancel(PublishCancelRequest publishCancelRequest){
        //验证时间戳是否合法
        if (CommonUtil.isTimeStampInValid(publishCancelRequest.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        String publishIds = publishCancelRequest.getPublishIds();
        String[] ids = publishIds.split(Constants.DELIMITER_COMMA);
        List<Integer> cancelledPublishIdList = new ArrayList<>();
        for(String publishId : ids){
            List<Integer> states = new ArrayList<>();
            states.add(Status.OrderParking.UNCONFIRMED.getInt());
            states.add(Status.OrderParking.USED.getInt());
            Integer number = orderParkingRepository.findOrderParkingByPublishIdAndStates(Integer.valueOf(publishId),states,Status.OrderParking.RESERVED.getInt());
            //如果车位被占用，PUBLISH表状态改为待取消，在车位空出来时取消发布  反之直接取消发布
            if(!number.equals(Constants.MIN_NON_NEGATIVE_INTEGER)){
                publishRepository.changeStateByIdAndState(Integer.valueOf(publishId),Status.Publish.TOBECANCELLED.getInt(),Status.Common.VALID.getInt());
            }else{
                //发布状态改为失效
                publishRepository.updateState(Integer.valueOf(publishId),Status.Common.INVALID.getInt());
                shareRepository.updateState(Integer.valueOf(publishId),Status.Common.INVALID.getInt(),Status.Common.VALID.getInt());
                cancelledPublishIdList.add(Integer.valueOf(publishId));
            }
        }

        HashSet<Integer> parklotIdSets = new HashSet<>();
        if(cancelledPublishIdList.size() != Constants.EMPTY_CAPACITY){
            for(Integer publishId : cancelledPublishIdList){
//                Publish publish = publishRepository.findOne(publishId);
//                Integer parklocId = publish.getParklocId();
                ParklotIdAdParklocIdData parklotIdAdParklocIdData = publishMapper.queryParklocIdAdParklotIdByPublishId(publishId);
                Integer parklocId = parklotIdAdParklocIdData.getParklocId();
                //如果车位没有发布的车位，更新车位状态
                List<Integer> states = new ArrayList<>();
                states.add(Status.Publish.TOBEALTER.getInt());
                states.add(Status.Publish.TOBECANCELLED.getInt());
                states.add(Status.Common.VALID.getInt());
                List<Publish> publishs = publishRepository.findByParklocIdAndStates(parklocId,states);
                if(publishs.size() == Constants.MIN_NON_NEGATIVE_INTEGER){
                    parklocRepository.updateState(parklocId,Status.Parkloc.UNPUBLISHED.getInt());
                    // 更新该车位对应的车场的parklot_amount表的数据
//                    Integer parklotId = parklotMapper.queryParklotIdByParklocId(parklocId);
//                    parklotIdSets.add(parklotId);
                    parklotIdSets.add(parklotIdAdParklocIdData.getParklotId());
                }
            }
        }
        if (parklotIdSets.size() > 0) {
            log.info("取消发布的车场id parklotIdSets = " + parklotIdSets);
            for (Integer parklotId : parklotIdSets) {
                parklotAmountService.updateParklotAmountInfoByParklotId(parklotId, "cancel 取消发布车位");
            }
        }
        return RespUtil.successResp();
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp alter(PublishListAlterRequest publishListAlterRequest){
        //验证时间戳是否合法
        if (CommonUtil.isTimeStampInValid(publishListAlterRequest.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        String publishIds = publishListAlterRequest.getPublishIds();
        String[] ids = publishIds.split(Constants.DELIMITER_COMMA);
        Integer mode = publishListAlterRequest.getMode();
        Integer parklotId = publishListAlterRequest.getParklot_id();
        String dayOfWeek = publishListAlterRequest.getDay_of_week();
        Long startTime = publishListAlterRequest.getStart_time();
        Long endTime = publishListAlterRequest.getEnd_time();
        //校验时间的合法性
        if (!checkTime(parklotId, startTime, endTime)) {
            throw new QhieException(Status.ApiErr.PUBLISH_TIME_ILLEGAL);
        }
        Integer repetNumber = Constants.MIN_NON_NEGATIVE_INTEGER;
        Integer tobeAlterNmuber = Constants.MIN_NON_NEGATIVE_INTEGER;
        Integer successNumber = Constants.MIN_NON_NEGATIVE_INTEGER;
        //循环判断发布是否合法
        for(String publishId : ids){
            Parkloc parkloc = parklocRepository.findByPublishId(Integer.valueOf(publishId),Status.Common.INVALID.getInt());
            if(null == parkloc){
                continue;
            }
            Integer parklocId = parkloc.getId();
            //  检测是否重复发布
            List<Publish> publishList = publishRepository.findByParklocIdAndStateNotAndIdNot(parklocId, Status.Common.INVALID.getInt(),Integer.valueOf(publishId));
            if (isPublishTimeRepetition(publishList, parklotId, startTime, endTime, dayOfWeek, mode)) {
                log.error("parklocId: " + parklocId + " result: " + Status.ApiErr.REPEAT_PUBLISH_TIME.getMsg());
                repetNumber++;
                continue;
            }
            List<Integer> states = new ArrayList<>();
            states.add(Status.OrderParking.UNCONFIRMED.getInt());
            states.add(Status.OrderParking.USED.getInt());
            Integer number = orderParkingRepository.findOrderParkingByPublishIdAndStates(Integer.valueOf(publishId),states,Status.OrderParking.RESERVED.getInt());
            Publish publish  = publishRepository.findOne(Integer.valueOf(publishId));
            //如果车位被占用，PUBLISH表状态改为待修改，在车位空出来时修改发布  反之直接修改发布
            if(!number.equals(Constants.MIN_NON_NEGATIVE_INTEGER)){
                //如果是待修改状态再修改表中当前的发布时间
                if(Status.Publish.TOBEALTER.getInt().equals(publish.getState())){
                    publishRepository.alterSecondPublish(Integer.valueOf(publishId),startTime,endTime,TimeUtil.makeTimeRange(startTime,endTime),mode,dayOfWeek,Status.Publish.TOBEALTER.getInt());
                }else{
                    publishRepository.alterPublish(Integer.valueOf(publishId),startTime,endTime,TimeUtil.makeTimeRange(startTime,endTime),mode,dayOfWeek,Status.Publish.TOBEALTER.getInt());
                }
                tobeAlterNmuber++;
            }else{
                //如果是待修改状态再修改表中当前的发布时间
                if(Status.Publish.TOBEALTER.getInt().equals(publish.getState())){
                    publishRepository.alterSecondPublish(Integer.valueOf(publishId),startTime,endTime,TimeUtil.makeTimeRange(startTime,endTime),mode,dayOfWeek,Status.Publish.TOBEALTER.getInt());
                }else{
                    publishRepository.alterPublish(Integer.valueOf(publishId),startTime,endTime,TimeUtil.makeTimeRange(startTime,endTime),mode,dayOfWeek,Status.Common.VALID.getInt());
                }

                //共享状态改为失效
                shareRepository.updateState(Integer.valueOf(publishId),Status.Common.INVALID.getInt(),Status.Common.VALID.getInt());
                //重新生成共享时间
                int todayOfWeek = TimeUtil.getDayOfWeekToday();
                this.share(mode,parklocId,Integer.valueOf(publishId),startTime,endTime,dayOfWeek,todayOfWeek);
                successNumber++;
            }
        }
        PublishFaiNumberRepData data = new PublishFaiNumberRepData();
        data.setRepetNumber(repetNumber);
        data.setTobeAlterNmuber(tobeAlterNmuber);
        data.setSuccessNumber(successNumber);
        return RespUtil.successResp(data);
    }


    private void share(Integer mode,Integer parklocId,Integer publishId,Long startTime,Long endTime,String dayOfWeek,int todayOfWeek){
        //如果不是重复发布直接插入到Share表
        Share share;
        if(Constants.SINGLE_MODE.equals(mode)){
            share = new Share(parklocId,publishId,startTime,endTime,Status.Common.VALID.getInt());
            if(null == shareRepository.save(share)){
                log.error("parklocId:"+parklocId +" result:Share "+Status.ApiErr.INSERT_ERROR.getMsg());
                throw new QhieException(Status.ApiErr.INSERT_ERROR);
            }
        }else{
            //如果重复发布中包含了今天插入到Share表
            if (dayOfWeek.contains(String.valueOf(todayOfWeek))) {
                share = new Share(parklocId,publishId,startTime,endTime,Status.Common.VALID.getInt());
                if(null == shareRepository.save(share)){
                    log.error("parklocId:"+parklocId +" result:Share "+Status.ApiErr.INSERT_ERROR.getMsg());
                    throw new QhieException(Status.ApiErr.INSERT_ERROR);
                }
            }
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp batchAdd(PublishBatchAddRequest publishBatchAddRequest){
        //验证时间戳是否合法
        if (CommonUtil.isTimeStampInValid(publishBatchAddRequest.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        String parklocIds = publishBatchAddRequest.getParklocIds();
        String[] ids = parklocIds.split(Constants.DELIMITER_COMMA);
        Long startTime = publishBatchAddRequest.getStart_time();
        Long endTime = publishBatchAddRequest.getEnd_time();
        Integer mode = publishBatchAddRequest.getMode();
        String dayOfWeek = publishBatchAddRequest.getDay_of_week();
        Integer parklotId = publishBatchAddRequest.getParklot_id();
        if (ParamUtil.isInValidDayOfWeeks(mode, dayOfWeek)) {
            throw new QhieException(Status.ApiErr.PARAMS_PUBLISH_ADD);
        }
        //校验时间的合法性
        if (!checkTime(parklotId, startTime, endTime)) {
            throw new QhieException(Status.ApiErr.PUBLISH_TIME_ILLEGAL);
        }
        List<Integer> parklocIdList = new ArrayList<>();
        for(String parklocId : ids){
            Publish publish = new Publish(Integer.valueOf(parklocId),startTime,endTime, TimeUtil.makeTimeRange(startTime, endTime), mode, dayOfWeek, Status.Common.VALID.getInt());
            //保存到发布表
            Publish resPublish;
            if(null == (resPublish = publishRepository.save(publish))){
                log.error("parklocId:"+parklocId +" result:"+Status.ApiErr.INSERT_ERROR.getMsg());
                throw new QhieException(Status.ApiErr.INSERT_ERROR);
            }
            int todayOfWeek = TimeUtil.getDayOfWeekToday();
            //如果不是重复发布直接插入到Share表
            this.share(resPublish.getMode(),Integer.valueOf(parklocId),resPublish.getId(),resPublish.getStartTime(),resPublish.getEndTime(),resPublish.getDayOfWeek(),todayOfWeek);
            parklocIdList.add(Integer.valueOf(parklocId));
        }
        //修改车位状态为待发布
        parklocRepository.updateStateByParklocIds(parklocIdList,Status.Parkloc.PUBLISHED.getInt());
        //  更新该车场的parklot_amount表的数据
        parklotAmountService.updateParklotAmountInfoByParklotId(parklotId, "batchAdd 批量发布车位");
        return RespUtil.successResp();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
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

        //  更新该车场的parklot_amount表的数据
        if (orderParking.getParklotId() != null) {
            parklotAmountService.updateParklotAmountInfoByParklotId(orderParking.getParklotId(), "dealWithToBePublish 处理待取消和待修改的发布");
        }
    }





    /**
     *  批量插入Publish
     * @param list
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchInsert(List<Publish> list) {
        int size =  list.size();
        for (int i = 0; i < size; i++) {
            Publish dd = list.get(i);
            em.persist(dd);
            // 每1000条数据执行一次，或者最后不足1000条时执行
            if (i % 1000 == 0 || i==(size-1)) {
                em.flush();
                em.clear();
            }
        }
    }


}

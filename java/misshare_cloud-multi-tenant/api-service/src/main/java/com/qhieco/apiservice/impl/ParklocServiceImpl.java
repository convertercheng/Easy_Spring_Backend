package com.qhieco.apiservice.impl;

import com.qhieco.apiservice.ParklocService;
import com.qhieco.apiservice.ParklotAmountService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.commonentity.*;
import com.qhieco.commonrepo.*;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.mapper.*;
import com.qhieco.push.QhMessageTemplate;
import com.qhieco.push.QhMessageType;
import com.qhieco.request.api.AllowPublishCountQueryRequest;
import com.qhieco.request.api.LockListRequest;
import com.qhieco.request.api.ParklocAddRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.api.*;
import com.qhieco.time.ParklocInfo;
import com.qhieco.time.ParklotIdAdParklocIds;
import com.qhieco.time.TimePeriod;
import com.qhieco.util.CommonUtil;
import com.qhieco.util.QhPushUtil;
import com.qhieco.util.RespUtil;
import com.qhieco.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/6 13:57
 * <p>
 * 类说明：
 * 车位service实现
 */
@Service
@Slf4j
public class ParklocServiceImpl implements ParklocService {

    @Autowired
    ApplyParklocRepository applyParklocRepository;

    @Autowired
    UserMobileRepository userMobileRepository;

    @Autowired
    UserExtraInfoRepository userExtraInfoRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    AreaRepository areaRepository;

    @Autowired
    private ParklocRepository parklocRepository;

    @Autowired
    private ParklotRepository parklotRepository;

    @Autowired
    private PublishRepository publishRepository;

    @Autowired
    private ParklocMapper parklocMapper;

    @Autowired
    private ParklotMapper parklotMapper;

    @Autowired
    private LockMapper lockMapper;

    @Autowired
    private AccessMapper accessMapper;
    @Autowired
    private ParklotAmountService parklotAmountService;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Resp addParkloc(ParklocAddRequest parklocAddRequest) {
        //验证时间戳是否合法
        if (CommonUtil.isTimeStampInValid(parklocAddRequest.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        //验证手机用户是否存在
        Integer userId = parklocAddRequest.getUser_id();
        if (null == userMobileRepository.findOne(userId)) {
            throw new QhieException(Status.ApiErr.NONEXISTENT_USER);
        }
        //验证地区是否存在
        Integer areaId = parklocAddRequest.getArea_id();
        if (null == areaRepository.findOne(areaId)) {
            throw new QhieException(Status.ApiErr.NONEXISTENT_AREA);
        }
        Long now = System.currentTimeMillis();
        //保存车位申请
        String parklotName = parklocAddRequest.getParklot_name();
        String contactPhone = parklocAddRequest.getContact_phone();
        ApplyParkloc applyParkloc = new ApplyParkloc(userId, areaId, parklotName, contactPhone, now, Status.Apply.PROCESSING.getInt());
        ApplyParkloc resApplyParkloc = applyParklocRepository.save(applyParkloc);
        if (null == resApplyParkloc) {
            throw new QhieException(Status.ApiErr.INSERT_ERROR);
        }
        UserExtraInfo userExtraInfo = userExtraInfoRepository.findByMobileUserId(userId);
        String content = QhMessageTemplate.APPLY_PARKLOC_PROCESSING;
        //极光推送
        if (null != userExtraInfo && !StringUtils.isEmpty(userExtraInfo.getJpushRegId())) {
            QhPushUtil.getInstance().sendQhPush(userExtraInfo.getJpushRegId(), QhMessageType.WALLET, content, "");
        }
        Message message = new Message(userId, 0, QhMessageType.WALLET.getTitle(), content, null, Constants.MESSAGE_TYPE_PERSONAL, Constants.MESSAGE_KIND_JPUSH, Status.Common.VALID.getInt(), now);
        messageRepository.save(message);
        return RespUtil.successResp();
    }

    @Override
    public List<PublishParklocRespData> queryPublishParklocList(Integer userId, Integer state, Integer pageNum) {
        int startPage = pageNum * Constants.PAGE_SIZE;
        List<PublishParklocRespData> publishParklocRespDataList = null;
        if (StringUtils.isEmpty(state) || -1 == state) {
            // 全部车位
            publishParklocRespDataList = parklocMapper.queryAllParklocListByUserIdAdState(userId, state, startPage, Constants.PAGE_SIZE);
        } else {
            // 已发布车位：循环发布的所有车位 和  单次发布的有效车位
            publishParklocRespDataList = parklocMapper.queryPublishedParklocsByUserId(userId, startPage, Constants.PAGE_SIZE);
        }

        // 发布状态的车位才查询发布信息
        if (publishParklocRespDataList.size() > 0 && state.equals(Status.Parkloc.PUBLISHED.getInt())) {
            List<PublishRespData> publishRespDataList;
            for (PublishParklocRespData publishParklocRespData : publishParklocRespDataList) {
                // 查询车位发布信息
                publishRespDataList = parklocMapper.queryPublishListByParklocIdAdState(publishParklocRespData.getParklocId(),
                        Status.Common.VALID.getInt(), Status.Publish.TOBECANCELLED.getInt(), Status.Publish.TOBEALTER.getInt());
                publishParklocRespData.setPublishList(publishRespDataList);
            }
        }
        return publishParklocRespDataList;
    }

    @Override
    public AllowPublishCountRespData queryAllowPublishInfo(AllowPublishCountQueryRequest allowPublishCountQueryRequest) {
        AllowPublishCountRespData allowPublishCountRespData = new AllowPublishCountRespData();
        getCurrentAvailableParklocs(allowPublishCountQueryRequest, allowPublishCountRespData);
        return allowPublishCountRespData;
    }

    private void getCurrentAvailableParklocs(AllowPublishCountQueryRequest allowPublishCountQueryRequest, AllowPublishCountRespData allowPublishCountRespData) {
        Integer userId = allowPublishCountQueryRequest.getUser_id();
        List<Integer> parklotIds = parklotRepository.findIdsByMobileUserId(userId);
        if (null == parklotIds || Constants.EMPTY_CAPACITY == parklotIds.size()) {
            throw new QhieException(Status.ApiErr.NONEXISTENT_PARKLOT_OF_THE_USER);
        } else if (Constants.ONE_CAPACITY < parklotIds.size()) {
            throw new QhieException(Status.ApiErr.NOT_SUPPORT_ONE_ADMIN_TO_MANY_PARKLOTS);
        }
        Integer parklotId = parklotIds.get(Constants.FIRST_INDEX);
        log.info("parklot id is {}", parklotId);
        /*
         * 1. 获取用户手下的所有车位 -> 直接获取用户下的车位id
         */
        List<Integer> parklocIds = parklocRepository.findByMobileUserId(userId);
        if (null == parklocIds || Constants.EMPTY_CAPACITY == parklocIds.size()) {
            allowPublishCountRespData.setCount(Constants.EMPTY_CAPACITY);
            allowPublishCountRespData.setParklocIds(Constants.EMPTY_STRING);
            return;
        }
        Integer availableCount = Constants.EMPTY_CAPACITY;
        StringBuilder stringBuilder = new StringBuilder();
        Long startTime = allowPublishCountQueryRequest.getStart_time();
        Long endTime = allowPublishCountQueryRequest.getEnd_time();
        Integer mode = allowPublishCountQueryRequest.getMode();
        String dayOfWeek = allowPublishCountQueryRequest.getDay_of_week();
        String minPublishInterval = getMinPublishInterval(parklotId);
        /*
         * 2. 查看发过来的请求与当前车位已发布的情况是否重复，如果重复，则减1
         */
        for (Integer parklocId : parklocIds) {
            /*
             * 检查每个车位的发布情况
             */
            log.info("check parkloc: {}", parklocId);
            /*
             * 对于每个车位，查询mode为1的或者mode为0且发布结束时间end_time > 当前时间的publish
             */
            List<Publish> publishList = parklocMapper.findValidPublishList(parklocId);
            if (!isPublishTimeRepetition(publishList, minPublishInterval, startTime, endTime, dayOfWeek, mode)) {
                availableCount++;
                stringBuilder.append(parklocId).append(Constants.DELIMITER_COMMA);
            }
        }
        String parklocIdStrs = Constants.EMPTY_STRING;
        log.info("parklocIds: {}", stringBuilder.toString());
        if (stringBuilder.length() >= Constants.ONE_CAPACITY) {
            parklocIdStrs = stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
        }
        allowPublishCountRespData.setCount(availableCount);
        allowPublishCountRespData.setParklocIds(parklocIdStrs);
        allowPublishCountRespData.setParklotId(parklotId);
    }

    @Override
    public List<PublishParklocRespData> queryParklocPublishInfoByNumber(Integer userId, String parklocNumber) {
        return parklocMapper.queryParklocPublishInfoByNumber(userId, parklocNumber, Status.Common.INVALID.getInt(),
                Status.Publish.TOBECANCELLED.getInt(), Status.Publish.TOBEALTER.getInt());
    }

    @Override
    public List<ParklocPublishInfoRespData> queryParklocPublishInfoByTime(Integer userId, Long startTime, Long endTime, int pageNum) {
        int startPage = pageNum * Constants.PAGE_SIZE;
        return parklocMapper.queryParklocPublishInfoByTime(userId, startTime, endTime, Status.Common.INVALID.getInt(),
                Status.Publish.TOBECANCELLED.getInt(), Status.Publish.TOBEALTER.getInt(), Status.PublishMode.ONCE.getInt(),
                Status.PublishMode.LOOP.getInt(), startPage, Constants.PAGE_SIZE);
    }

    @Override
    public ParklocLockAccessListRespData queryParklocLockListByUserId(Integer userId) {
        ParklocLockAccessListRespData parklocLockAccessListRespData = new ParklocLockAccessListRespData();
        parklocLockAccessListRespData.setLockList(lockMapper.queryParklocLockListByUserId(userId));
        parklocLockAccessListRespData.setAccessList(accessMapper.queryAccessListByUserId(userId, Status.Common.VALID.getInt()));
        log.info(" 车位锁列表 - " + parklocLockAccessListRespData + ", userId = " + userId);
        return parklocLockAccessListRespData;
    }

    /**
     * 检验发布时间是否与已发布的重复
     *
     * @param publishList 发布的车位列表
     * @return 与该车位已发布的时间段重复
     */
    private boolean isPublishTimeRepetition(List<Publish> publishList, String minPublishInterval, Long startTime, Long endTime, String dayOfWeeks, Integer mode) {
        boolean result = false;
        HashMap<Integer, ArrayList<TimePeriod>> publishPeriod;
        publishPeriod = getPublishInfoOneWeek(publishList, Integer.valueOf(minPublishInterval) * TimeUtil.MINUTE_2_MSECONDS);
        /*
         * 2 判断是否重复
         */
        String timeRange = TimeUtil.makeTimeRange(startTime, endTime);
        String[] timeRanges = timeRange.split(Constants.DELIMITER_POUND);
        TimePeriod firstPeriod = new TimePeriod(timeRanges[0]);
        log.info("first timePeriod is {}", firstPeriod);
        if (Status.PublishMode.ONCE.getInt().equals(mode)) {
            int today = TimeUtil.dayOfWeek(startTime);
            result = TimeUtil.isTimePeriodInList(firstPeriod, publishPeriod.get(today), minPublishInterval);
            if (result) {
                return true;
            }
            if (timeRanges.length > Constants.ONE_CAPACITY) {
                TimePeriod secondPeriod = new TimePeriod(timeRanges[1]);
                log.info("second timePeriod is {}", secondPeriod);
                result = TimeUtil.isTimePeriodInList(secondPeriod, publishPeriod.get((today + 1) % 7), minPublishInterval);
            }
        } else if (Status.PublishMode.LOOP.getInt().equals(mode)) {
            for (String day : dayOfWeeks.split(Constants.DELIMITER_COMMA)) {
                result = TimeUtil.isTimePeriodInList(firstPeriod, publishPeriod.get(Integer.valueOf(day)), minPublishInterval);
                if (result) {
                    return true;
                }
            }
            if (timeRanges.length > Constants.ONE_CAPACITY) {
                TimePeriod secondPeriod = new TimePeriod(timeRanges[1]);
                log.info("second timePeriod is {}", secondPeriod);
                for (String day : dayOfWeeks.split(Constants.DELIMITER_COMMA)) {
                    result = TimeUtil.isTimePeriodInList(secondPeriod, publishPeriod.get((Integer.valueOf(day) + 1) % 7), minPublishInterval);
                }
            }
        }
        return result;
    }

    /**
     * 获取最小发布时间间隔
     *
     * @param parklotId 停车场id
     * @return 发布时间最短间隔
     */
    private String getMinPublishInterval(Integer parklotId) {
        String value = parklotMapper.queryMinPublishTimeInterval(parklotId, Constants.MIN_PUBLISH_INTERVAL);
        if (StringUtils.isEmpty(value)) {
            value = Constants.MIN_PUBLISH_INTERVAL_DEFAULT;
        }
        return value;
    }

    /**
     * 根据查询出来的发布信息构建一个映射表， 星期几 ---> list { time period 1, time period 2, time period 3 }
     *
     * @param publishList 发布信息list
     * @return HashMap
     */
    private HashMap<Integer, ArrayList<TimePeriod>> getPublishInfoOneWeek(List<Publish> publishList, Long minPublishInterval) {
        HashMap<Integer, ArrayList<TimePeriod>> publishPeriod;
        // 减少resize()的开销
        publishPeriod = new HashMap<>(250);
        Integer[] weeks = {0, 1, 2, 3, 4, 5, 6};
        for (Integer day : weeks) {
            publishPeriod.put(day, new ArrayList<>());
        }
        for (Publish publish : publishList) {
            log.info("publish: {}", publish);
            createPublishTime(publishPeriod, publish.getTimeRange(), publish.getMode(), publish.getDayOfWeek(), minPublishInterval, publish.getStartTime(), publish.getEndTime());
            //如果发布是待修改状态，把上一次的发布时间段拿出来比较
            Integer publishState = publish.getState();
            if (Status.Publish.TOBEALTER.getInt().equals(publishState)) {
                createPublishTime(publishPeriod, publish.getLastTimeRange(), publish.getLastMode(), publish.getLastDayOfWeek(), minPublishInterval, publish.getLastStartTime(), publish.getLastEndTime());
            }
        }
        log.info("------------- publishPeriod ---- start ---------");
        for (Integer day : weeks) {
            log.info("{}: {}", day, publishPeriod.get(day));
        }
        log.info("------------- publishPeriod ----- end --------");
        return publishPeriod;
    }

    private void createPublishTime(HashMap<Integer, ArrayList<TimePeriod>> publishPeriod, String publishTimeRange, Integer mode, String dayOfWeek, Long minPublishInterval, Long startTime, Long endTime) {
        String[] timeRange = publishTimeRange.split(Constants.DELIMITER_POUND);
        if (Status.PublishMode.LOOP.getInt().equals(mode)) {
            for (String day : dayOfWeek.split(Constants.DELIMITER_COMMA)) {
                log.info("day is {}", day);
                publishPeriod.get(Integer.valueOf(day)).add(new TimePeriod(timeRange[0]));
                if (timeRange.length > 1) {
                    publishPeriod.get((Integer.valueOf(day) + 1) % 7).add(new TimePeriod(timeRange[1]));
                }
            }
        } else {
            if (startTime < endTime + minPublishInterval) {
                log.info("enter single mode-----------------------");
                int day = TimeUtil.dayOfWeek(startTime);
                log.info("timeRange: {}", Arrays.asList(timeRange));
                publishPeriod.get(day).add(new TimePeriod(timeRange[0]));
                if (timeRange.length > 1) {
                    publishPeriod.get((day + 1) % 7).add(new TimePeriod(timeRange[1]));
                }
                log.info("single mode: {}: {}", day, publishPeriod.get(day));
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateParklocAdParklotInfo(ParklotIdAdParklocIds parklotIdAdParklocIds) {
        List<ParklocInfo> parklocInfos = parklotIdAdParklocIds.getParklocInfos();
        for (ParklocInfo parklocInfo : parklocInfos) {
            // 更新车位状态为未发布
            parklocMapper.updateParklocStateByParklocId(parklocInfo.getParklocId(), Status.Parkloc.UNPUBLISHED.getInt());
        }
        //  更新该车场的parklot_amount表的数据
        parklotAmountService.updateParklotAmountInfoByParklotId(parklotIdAdParklocIds.getParklotId(), "定时器扫描过期的publish数据");
    }

    @Override
    public Resp lockList(LockListRequest lockListRequest) {
        //验证时间戳是否合法
        if (CommonUtil.isTimeStampInValid(lockListRequest.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        Integer pageNum = lockListRequest.getPageNum();
        String parklocNum = lockListRequest.getParklocNum();
        Integer userId = lockListRequest.getUserId();
        int startPage = pageNum * Constants.PAGE_SIZE_50;
        List<ParklocLockRespData> parklocLockRespDataList = parklocMapper.queryParklocListByUserIdAndParklocNum(userId, parklocNum, Status.Parkloc.UNPUBLISHED.getInt(), startPage, Constants.PAGE_SIZE_50);
        return RespUtil.successResp(parklocLockRespDataList);
    }


}

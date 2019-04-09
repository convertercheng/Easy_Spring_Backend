package com.qhieco.apischedule.timer;

import com.qhieco.apiservice.ParklocService;
import com.qhieco.apiservice.ParklotAmountService;
import com.qhieco.commonentity.Share;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.mapper.ParklocMapper;
import com.qhieco.mapper.PublishMapper;
import com.qhieco.mapper.ShareMapper;
import com.qhieco.time.ParklocShare;
import com.qhieco.time.ParklotIdAdParklocIds;
import com.qhieco.time.ShareTimeOutInfo;
import com.qhieco.time.ValidPublishInfo;
import com.qhieco.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/28 10:49
 * <p>
 * 类说明：
 * 关于车位的定时器
 */
@Component
@Slf4j
public class ParklocTimer {
    @Autowired
    private ParklocService parklocService;

    @Autowired
    private ParklocMapper parklocMapper;

    @Autowired
    private ShareMapper shareMapper;

    @Autowired
    private PublishMapper publishMapper;

    @Autowired
    private ParklotAmountService parklotAmountService;

    /**
     * 扫描过期的车位共享时间
     */
//    @Scheduled(cron = "0 0/1 * * * ?")
    public void scanShareTimeout() {
        // 查询所有的没被使用，且时间段过期的车位共享时间
        List<ParklocShare> parklocShareList = parklocMapper.queryTimeoutShareList(Status.OrderParking.SYS_CANCELED.getInt(),
                Status.OrderParking.TIMEOUT.getInt(), Status.OrderParking.USER_CANCELED.getInt(), Status.OrderParking.CUST_SERVICE_CANCELED.getInt(),
                Status.Common.INVALID.getInt());
        log.warn(" scanShareTime 过期且没有被使用的共享信息数量： " + parklocShareList.size());
        // 批量更新share的状态为不可用
        if (parklocShareList.size() > 0) {
            shareMapper.updateBatchShareStateByIds(parklocShareList, Status.Common.INVALID.getInt());
        }
    }

    /**
     * 扫描车位单次发布模式过期的
     */
//    @Scheduled(cron = "0 0/1 * * * ?")
    @Scheduled(cron = "0 0/6 * * * ?")
    public void scanPublishTimeout() {
        long now = System.currentTimeMillis();
        log.warn("扫描车位单次发布模式过期的 scanPublishTimeout  start --------- ");
        publishMapper.updatePublishOnceTimeout(Status.Common.VALID.getInt(), Status.Common.INVALID.getInt(), Status.PublishMode.ONCE.getInt(), now);

        //如果车位 在Publish表没有有效的发布信息 并且 车位状态不是未发布和已预约状态，则更新车位状态为未发布
        HashMap params = new HashMap(8);
        params.put("tobealter", Status.Publish.TOBEALTER.getInt());
        params.put("tobecancelled", Status.Publish.TOBECANCELLED.getInt());
        params.put("valid", Status.Common.VALID.getInt());
        params.put("unpublished", Status.Parkloc.UNPUBLISHED.getInt());
        params.put("reserved", Status.Parkloc.RESERVED.getInt());
        List<ParklotIdAdParklocIds> parklotIdAdParklocIds = publishMapper.queryNoPublishedStateParklots(params);

        log.warn("需要恢复车场车位数量的车场数为：" + parklotIdAdParklocIds.size() + " 个。");
        if (parklotIdAdParklocIds.size() > 0) {
            for (ParklotIdAdParklocIds parklotIdAdParklocId : parklotIdAdParklocIds) {
                try {
                    parklocService.updateParklocAdParklotInfo(parklotIdAdParklocId);
                } catch (Exception e) {
                    log.error(" 恢复车场车位数量异常，parklotId = " + parklotIdAdParklocId.getParklotId() + " \n " + e);
                }
            }
        }

        //  查询t_share表共享时间过期的数据，更新parklot_amount的数据
        int timeInterval = TimeUtil.getMinChargingPeriod() * 60 * 1000 + TimeUtil.getTimeInterval() * 60 * 1000;
        List<ShareTimeOutInfo> shareTimeOutInfos = shareMapper.queryShareTimeOutList(timeInterval);
        log.warn("t_share表共享时间过期的数据数量， size =" + shareTimeOutInfos.size());

        for (ShareTimeOutInfo shareTimeOutInfo : shareTimeOutInfos) {
            log.warn("t_share表共享时间过期的数据， shareTimeOutInfo:" + shareTimeOutInfo);
            try {
                parklotAmountService.updateParklotAmountInfoByParklotId(shareTimeOutInfo.getParklotId(), "定时器扫描t_share表过期的数据");

                List<ParklocShare> parklocShareList = shareTimeOutInfo.getParklocShareList();
                if (parklocShareList != null && parklocShareList.size() > 0) {
                    shareMapper.updateBatchShareStateByIds(parklocShareList, Status.Common.INVALID.getInt());
                }
            } catch (Exception e) {
                log.error(" t_share表共享时间过期的数据， 更新车场车位数量异常，parklotId = " + shareTimeOutInfo.getParklotId() + " \n " + e);
            }
        }
        log.warn("扫描车位单次发布模式过期的 scanPublishTimeout  end --------- ");
    }

    //    @Scheduled(cron = "0 0/1 * * * ?")
    @Scheduled(cron = "0 0/1 * * * ?")
    public void scanParklocReservable() {
        // 车位刚发布时不可以预约,在amount表不显示，定时查询是否可以预约，更新parklot_amount表
        int timeInterval = TimeUtil.getMinChargingPeriod() * 60 * 1000 + TimeUtil.getTimeInterval() * 60 * 1000;
        List<Integer> parklotIdList = publishMapper.queryReservateTimeList(Constants.ADVANCE_RESERVATION_TIME, timeInterval);
        log.warn("车位刚发布时不可以预约,在amount表不显示，定时查询是否可以预约 的数据：" + parklotIdList);
        for (Integer parklotId : parklotIdList) {
            try {
                parklotAmountService.updateParklotAmountInfoByParklotId(parklotId, "定时器扫描车位刚发布时不可预约, 目前可预约的情况");
            } catch (Exception e) {
                log.error("车位刚发布时不可以预约，目前可以预约,更新parklot_amount表，异常： " + e);
            }
        }
    }

    /**
     * 每天零点定时发布车位
     */
//    @Scheduled(cron = "0 0/1 * * * ?")
    //    @Scheduled(cron = "0 25 9 * * ?")
    @Scheduled(cron = "0 1 0 * * ?")
    public void publishToShare() {
        log.warn(" ---------- 每天零点定时发布车位publishToShare  start ---------- ");
        List<ValidPublishInfo> validPublishInfoList = publishMapper.queryValidPublishList(Status.PublishMode.LOOP.getInt(),
                Status.Common.VALID.getInt(), Status.Common.INVALID.getInt());
        List<Share> shareList = new ArrayList<>();
        Share share = null;
        Calendar calendar = Calendar.getInstance();
        long now = System.currentTimeMillis();
        for (ValidPublishInfo validPublishInfo : validPublishInfoList) {
            try {
                long startTime = validPublishInfo.getStartTime();
                long endTime = validPublishInfo.getEndTime();

                // 发布时间是否跨天
                if (TimeUtil.isSameDay(calendar, startTime, endTime)) {
                    endTime = TimeUtil.changeTime2day(endTime, 0);
                } else {
                    endTime = TimeUtil.changeTime2day(endTime, 1);
                }
                // 把时间转换成当天的日期
                startTime = TimeUtil.changeTime2day(startTime, 0);
                if (now >= endTime) {
                    continue;
                }

                share = new Share(validPublishInfo.getParklocId(), validPublishInfo.getPublishId(), startTime,
                        endTime, Status.Common.VALID.getInt());
                shareList.add(share);
            } catch (Exception e) {
                log.error("定时零点发布车位异常，validPublishInfo = " + validPublishInfo + ", /n 异常信息：" + e);
            }
        }
        log.warn("定时发布车位信息数量， " + shareList.size());
        if (shareList.size() > 0) {
            try {
                shareMapper.insertBatch(shareList);
            } catch (Exception e) {
                log.error("批量插入发布车位数据异常， " + e);
            }
        }
        log.warn(" ---------- 每天零点定时发布车位publishToShare  end ---------- ");
    }
}

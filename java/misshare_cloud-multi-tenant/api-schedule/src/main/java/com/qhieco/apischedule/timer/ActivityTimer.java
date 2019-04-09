package com.qhieco.apischedule.timer;

import com.qhieco.constant.Status;
import com.qhieco.mapper.ActivityMapper;
import com.qhieco.mapper.PrizeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/5/22 11:27
 * <p>
 * 类说明：
 * ${说明}
 */
@Component
@Slf4j
public class ActivityTimer {

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private PrizeMapper prizeMapper;

    /**
     * 扫描运营活动是否过期
     */
    /*@Scheduled(cron = "0 0/30 * * * ?")
    public void scanActivityTimeOut() {
        List<Integer> activityIds = activityMapper.queryTimeOutActivityList();
        if (activityIds != null && activityIds.size() > 0) {
            activityIds.forEach(id -> {
                activityMapper.updateState(id, Status.Common.INVALID.getInt());
            });
        }
    }*/

    @Scheduled(cron = "0 0/6 * * * ?")
    public void scanPrizeTimeOut() {
        // 更新奖品过期状态
        prizeMapper.updateTimeOutPrizeState();
    }
}

package com.qhieco.time;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/20 下午3:00
 * <p>
 * 类说明：
 *     时间段
 */
@Slf4j
public class TimePeriod {

    private static final long DAY_2_MSECONDS = 24 * 60 * 60 * 1000L;

    private Long startTime;

    private Long endTime;

    TimePeriod(Long startTime, Long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public TimePeriod(String timeRange) {
        this(Long.valueOf(timeRange.split(":")[0]), Long.valueOf(timeRange.split(":")[1]));
    }

    public Long getStartTime() {
        return startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    private boolean isTimePeriodRepetition(TimePeriod timePeriod) {
        Long startTime = timePeriod.getStartTime();
        Long endTime = timePeriod.getEndTime();
        return !(this.endTime <= startTime || this.startTime >= endTime);
    }

    public boolean isTimePeriodIntervalNoMoreThanConfigMinutes(TimePeriod timePeriod, int minutes) {
        Long startTime = timePeriod.getStartTime();
        Long endTime = timePeriod.getEndTime();
        Long thisStartTime = this.startTime;
        Long thisEndTime = this.endTime;
        log.info("将发布的时段：{} - {}, 已发布的时段：{} - {}", thisStartTime, thisEndTime, startTime, endTime);
        return !(startTime - thisEndTime >= minutes * 60 * 1000L || thisStartTime - endTime >= minutes * 60 * 1000L);
    }

    @Override
    public String toString() {
        return "TimePeriod{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}

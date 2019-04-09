package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应发布表
 */
@Data
@Entity
@Table(name = "t_publish")
public class Publish {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;

    @Column(name = "parkloc_id")
    private Integer parklocId;

    @Column(name = "start_time")
    private Long startTime;

    @Column(name = "end_time")
    private Long endTime;

    @Column(name = "time_range_format")
    private String timeRange;

    @Column(name = "mode")
    private Integer mode;

    @Column(name = "day_of_week")
    private String dayOfWeek;

    @Column(name = "last_start_time")
    private Long lastStartTime;

    @Column(name = "last_end_time")
    private Long lastEndTime;

    @Column(name = "last_time_range_format")
    private String lastTimeRange;

    @Column(name = "last_mode")
    private Integer lastMode;

    @Column(name = "last_day_of_week")
    private String lastDayOfWeek;

    @Column(name = "state")
    private Integer state;

    public Publish(){}

    public Publish(Integer parklocId, Long startTime, Long endTime, String timeRange, Integer mode, String dayOfWeek, Integer state) {
        this.parklocId = parklocId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.timeRange = timeRange;
        this.mode = mode;
        this.dayOfWeek = dayOfWeek;
        this.state = state;
    }

    @Override
    public String toString() {
        return "Publish{" +
                "parklocId=" + parklocId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", timeRange='" + timeRange + '\'' +
                ", mode=" + mode +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", lastStartTime=" + lastStartTime +
                ", lastEndTime=" + lastEndTime +
                ", lastTimeRange='" + lastTimeRange + '\'' +
                ", lastMode=" + lastMode +
                ", lastDayOfWeek='" + lastDayOfWeek + '\'' +
                ", state=" + state +
                '}';
    }
}
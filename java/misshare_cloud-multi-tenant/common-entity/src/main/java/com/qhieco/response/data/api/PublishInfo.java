package com.qhieco.response.data.api;

import lombok.Data;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/26 16:38
 * <p>
 * 类说明：
 * 车位发布信息
 */
@Data
public class PublishInfo {
    /**
     * 发布id
     */
    private Integer publishId;
    /**
     * 车位id
     */
    private Integer parklocId;
    /**
     * 发布开始时间
     */
    private Long startTime;
    /**
     * 发布结束时间
     */
    private Long endTime;
    /**
     * 发布模式，0是一次发布，1是循环发布
     */
    private Integer mode;
    /**
     * 发布周期，0-星期天,1,2....6-星期六.
     */
    private String dayOfWeek;
    /**
     * 发布状态
     */
    private int state;
}

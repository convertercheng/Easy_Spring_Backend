package com.qhieco.response.data.api;

import lombok.Data;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/13 16:41
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class PublishRespData {
    /**
     * 车位发布id
     */
    private Integer publishId;
    /**
     * 发布模式，0是一次发布，1是循环发布
     */
    private Integer mode;
    /**
     * 发布开始时间
     */
    private Long startTime;
    /**
     * 发布结束时间
     */
    private Long endTime;
    /**
     * 发布周期，0,1,2...
     */
    private String dayOfWeek;

    private Integer state;
}

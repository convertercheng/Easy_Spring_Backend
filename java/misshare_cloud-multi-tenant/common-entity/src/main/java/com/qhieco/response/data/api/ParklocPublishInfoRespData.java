package com.qhieco.response.data.api;

import lombok.Data;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/20 13:52
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class ParklocPublishInfoRespData {
    /**
     * 车位id
     */
    private Integer parklocId;
    /**
     * 车场id
     */
    private Integer parklotId;
    /**
     * 车位编号
     */
    private String parklocNumber;
    /**
     * 发布开始时间
     */
    private Long startTime;
    /**
     * 发布结束时间
     */
    private Long endTime;
    /**
     * 状态：待取消,待修改 > 已被预约
     */
    private Integer state;
    /**
     * 发布模式，0是一次发布，1是循环发布
     */
    private Integer mode;
    /**
     * 发布周期，0,1,2...
     */
    private String dayOfWeek;

    private Integer publishId;

}

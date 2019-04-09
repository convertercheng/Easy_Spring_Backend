package com.qhieco.response.data.api;

import lombok.Data;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/8 11:22
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class ReserveTimeData {

    private Integer id;
    /**
     * 入场时间
     */
    private Long startTime;
    /**
     * 离场时间
     */
    private Long endTime;

    private Integer parklocId;
}

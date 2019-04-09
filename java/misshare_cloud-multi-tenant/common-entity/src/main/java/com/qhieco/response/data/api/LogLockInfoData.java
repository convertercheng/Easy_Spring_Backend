package com.qhieco.response.data.api;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/7/17 11:39
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class LogLockInfoData {
    private Integer lockId;
    /**
     * 车位锁摇臂状态
     */
    private Integer rockerState;
    /**
     * 车位锁电量
     */
    private BigDecimal battery;
}

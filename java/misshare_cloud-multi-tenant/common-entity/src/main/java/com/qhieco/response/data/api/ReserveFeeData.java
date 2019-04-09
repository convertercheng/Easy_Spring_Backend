package com.qhieco.response.data.api;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/9 9:44
 * <p>
 * 类说明：
 * 预约费用规则
 */
@Data
public class ReserveFeeData {

    private Integer id;
    /**
     * 每个时间段结束时间
     */
    private Integer finishTime;

    private BigDecimal fee;

    public ReserveFeeData() {
    }

    public ReserveFeeData(Integer id, Integer finishTime, BigDecimal fee) {
        this.id = id;
        this.finishTime = finishTime;
        this.fee = fee;
    }
}

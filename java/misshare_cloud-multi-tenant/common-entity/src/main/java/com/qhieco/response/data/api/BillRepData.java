package com.qhieco.response.data.api;

import java.math.BigDecimal;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/2 14:01
 * <p>
 * 类说明：
 * ${说明}
 */
@lombok.Data
public class BillRepData {
    /**
     * 账单类型0:预约订单，1:停车订单，2:退款订单，3:充值订单，4:提现订单, 5 车位收入
     */
    private Integer type;
    /**
     * 订单更新时间
     */
    private Long updateTime;
    /**
     * 订单金额
     */
    private BigDecimal account;
    /**
     * 订单状态
     */
    private Integer state;
    /**
     * 订单号
     */
    private String serialNumber;
}

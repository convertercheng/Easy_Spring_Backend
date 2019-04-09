package com.qhieco.time;

import lombok.Data;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/27 16:20
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class UnconfirmedOrderInfo {
    /**
     * order_parking的id
     */
    private int orderId;
    /**
     * share表的id
     */
    private int shareId;
}

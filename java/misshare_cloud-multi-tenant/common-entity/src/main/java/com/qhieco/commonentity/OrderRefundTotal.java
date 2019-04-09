package com.qhieco.commonentity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 退款订单总表
 */
@Data
public class OrderRefundTotal {
    private Integer id;

    private String serialNumber;

    private BigDecimal fee;

    private Integer mobileUserId;

    private Byte channel;

    private Long createTime;

    private Integer state;
}
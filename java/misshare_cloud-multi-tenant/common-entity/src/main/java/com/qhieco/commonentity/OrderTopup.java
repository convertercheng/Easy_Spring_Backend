package com.qhieco.commonentity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 充值订单表
 */
@Data
public class OrderTopup {
    private Integer id;

    private String serialNumber;

    private Integer mobileUserId;

    private BigDecimal totalFee;

    private BigDecimal discountFee;

    private BigDecimal realFee;

    private String tradeNo;

    private Long createTime;

    private BigDecimal tripartiteFee;

    private Byte channel;

    private Integer state;
}
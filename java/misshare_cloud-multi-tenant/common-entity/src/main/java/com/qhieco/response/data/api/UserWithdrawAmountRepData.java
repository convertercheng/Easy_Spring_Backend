package com.qhieco.response.data.api;

import java.math.BigDecimal;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/6 15:18
 * <p>
 * 类说明：
 * ${说明}
 */
@lombok.Data
public class UserWithdrawAmountRepData {
    /**
     * 可提现金额
     */
    private BigDecimal withdrawAmount;

    /**
     * 银行卡id
     */
    private Integer bankcardId;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 银行卡号
     */
    private String bankNumber;
}

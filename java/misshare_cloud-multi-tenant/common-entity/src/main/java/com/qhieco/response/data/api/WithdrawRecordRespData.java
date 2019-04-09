package com.qhieco.response.data.api;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/14 10:31
 * <p>
 * 类说明：
 * 提现记录列表实体
 */
@Data
public class WithdrawRecordRespData {
    /**
     * 提现记录id
     */
    private Integer withdrawId;
    /**
     * 提现状态
     */
    private Integer state;
    /**
     * 提现金额
     */
    private BigDecimal balance;
    /**
     * 银行名称
     */
    private String bankName;
    /**
     * 银行卡号
     */
    private String bankNumber;
    /**
     * 提现时间：提现中的显示申请时间，提现成功和失败的显示完成时间
     */
    private Long withdrawTime;
    /**
     * 备注
     */
    private String message;
}

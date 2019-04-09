package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 用户余额表
 */
@Data
@Entity
@Table(name = "t_balance_user")
public class BalanceUser {

    public BalanceUser() {
    }

    public BalanceUser(Integer mobileUserId, BigDecimal balanceEarn, BigDecimal balanceTopupWx, BigDecimal balanceTopupAlipay, BigDecimal balanceInvoice, BigDecimal balanceFrozenReserve, BigDecimal balanceFrozenRefund, BigDecimal balanceFrozenWithdraw, Integer state) {
        this.mobileUserId = mobileUserId;
        this.balanceEarn = balanceEarn;
        this.balanceTopupWx = balanceTopupWx;
        this.balanceTopupAlipay = balanceTopupAlipay;
        this.balanceInvoice = balanceInvoice;
        this.balanceFrozenReserve = balanceFrozenReserve;
        this.balanceFrozenRefund = balanceFrozenRefund;
        this.balanceFrozenWithdraw = balanceFrozenWithdraw;
        this.state = state;
    }

    @Id
    @GeneratedValue
    private Integer id;

    /**
     * 移动用户id
     */
    @Column(name = "mobile_user_id")
    private Integer mobileUserId;

    /**
     * 赚得金额，车位收入
     */
    @Column(name = "balance_earn", nullable = false, insertable = false)
    private BigDecimal balanceEarn;

    /**
     * 充值金额：微信
     */
    @Column(name = "balance_topup_wx", nullable = false, insertable = false)
    private BigDecimal balanceTopupWx;

    /**
     * 充值金额：支付宝
     */
    @Column(name = "balance_topup_alipay", nullable = false, insertable = false)
    private BigDecimal balanceTopupAlipay;

    /**
     * 发票金额
     */
    @Column(name = "balance_invoice", nullable = false, insertable = false)
    private BigDecimal balanceInvoice;

    /**
     * 预约冻结金额
     */
    @Column(name = "balance_frozen_reserve", nullable = false, insertable = false)
    private BigDecimal balanceFrozenReserve;

    /**
     * 退款冻结金额
     */
    @Column(name = "balance_frozen_refund", nullable = false, insertable = false)
    private BigDecimal balanceFrozenRefund;

    /**
     * 提现冻结金额
     */
    @Column(name = "balance_frozen_withdraw", nullable = false, insertable = false)
    private BigDecimal balanceFrozenWithdraw;

    /**
     * 状态
     */
    @Column(name = "state")
    private Integer state;
}
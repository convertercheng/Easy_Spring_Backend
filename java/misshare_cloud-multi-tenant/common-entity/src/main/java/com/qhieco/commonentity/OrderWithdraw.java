package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应提现订单表
 */
@Data
@Entity
@Table(name = "t_order_withdraw")
public class OrderWithdraw {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "mobile_userId")
    private Integer mobileUserId;

    @Column(name = "web_userId")
    private Integer webUserId;

    @Column(name = "file_id")
    private Integer fileId;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "apply_time")
    private Long applyTime;

    @Column(name = "complete_time")
    private Long completeTime;

    @Column(name = "bank_card_id")
    private Integer bankCardId;

    @Column(name = "message")
    private String message;

    @Column(name = "state")
    private Integer state;

    public OrderWithdraw(){}

    public OrderWithdraw(String serialNumber, Integer mobileUserId, BigDecimal balance, Long applyTime, Integer bankCardId, Integer state){
        this.serialNumber = serialNumber;
        this.mobileUserId = mobileUserId;
        this.balance = balance;
        this.applyTime = applyTime;
        this.bankCardId = bankCardId;
        this.state = state;
    }
}
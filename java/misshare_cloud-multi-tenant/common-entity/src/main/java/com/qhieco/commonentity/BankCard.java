package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 银行卡表
 */
@Entity
@Table(name = "t_bank_card")
@Data
public class BankCard {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "mobile_user_id")
    private Integer mobileUserId;

    @Column(name = "name")
    private String name;

    @Column(name = "reserved_phone")
    private String reservedPhone;

    @Column(name = "bank_number")
    private String bankNumber;

    @Column(name = "bank")
    private String bank;

    @Column(name = "type")
    private String type;

    @Column(name = "state")
    private Integer state;

    @Column(name = "modify_time")
    private Long modifyTime;

    public BankCard(){}

    public BankCard(Integer mobileUserId,String name,String reservedPhone,String bankNumber,String bank,String type,Integer state,Long modifyTime){
        this.mobileUserId = mobileUserId;
        this.name = name;
        this.reservedPhone = reservedPhone;
        this.bankNumber = bankNumber;
        this.bank = bank;
        this.type = type;
        this.state = state;
        this.modifyTime = modifyTime;
    }
}
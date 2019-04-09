package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 优惠券表
 */
@Entity
@Table(name = "t_coupon")
@Data
public class Coupon {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "coupon_code")
    private String couponCode;

    @Column(name = "coupon_password")
    private String couponPassword;

    @Column(name = "coupon_type")
    private Integer couponType;

    @Column(name = "mobile_user_id")
    private Integer mobileUserId;

    @Column(name = "used_time")
    private Long usedTime;

    @Column(name = "used_money")
    private BigDecimal usedMoney;

    @Column(name = "activity_id")
    private Integer activityId;


//    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH },fetch = FetchType.EAGER , optional = true)
//    @JoinColumn(name="business_id")
//    private Business business;
    @Column(name="business_id")
    private  Integer businessId;

    @Column(name = "coupon_limit")
    private BigDecimal couponLimit;

    @Column(name = "create_time")
    private Long createTime;

    @Column(name = "state")
    private Integer state;

    @Column(name = "begin_time")
    private Long beginTime;

    @Column(name = "end_time")
    private Long endTime;

    @Transient
    private String businessName;

    @Transient
    private String stateStr;

    @Transient
    private String couponTypeStr;

    @Transient
    private String userPhone;

}
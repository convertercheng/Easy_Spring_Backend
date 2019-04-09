package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应优惠券模版表
 */
@Entity
@Table(name = "t_coupon_template")
@Data
public class CouponTemplate {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "coupon_type")
    private String couponType;

    @Column(name = "business_id")
    private Integer businessId;

    @Column(name = "coupon_limit")
    private BigDecimal couponLimit;

    @Column(name = "coupon_number_limit")
    private Integer couponNumberLimit;

    @Column(name = "coupon_left")
    private Integer couponLeft;

    @Column(name = "user_number_limit")
    private Integer userNumberLimit;

    @Column(name = "coupon_area")
    private Integer couponArea;

    @Column(name = "activity_id")
    private Integer activityId;

    @Column(name = "coupon_description")
    private String couponDescription;

    @Column(name = "coupon_state")
    private Integer couponState;

    @Column(name = "begin_time")
    private Long beginTime;

    @Column(name = "end_time")
    private Long endTime;
}
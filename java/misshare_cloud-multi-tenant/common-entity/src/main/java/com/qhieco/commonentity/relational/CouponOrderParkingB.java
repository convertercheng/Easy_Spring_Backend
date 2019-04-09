package com.qhieco.commonentity.relational;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 停车订单优惠券关联表
 */
@Entity
@Table(name = "b_coupon_order_parking")
@Data
public class CouponOrderParkingB {
    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private Integer orderId;

    @Column
    private Integer couponId;

    @Column
    private Long createTime;

    @Column
    private Long modifyTime;

    @Column
    private Integer state;

    public CouponOrderParkingB(){}

    public CouponOrderParkingB(Integer orderId,Integer couponId,Long createTime,Long modifyTime,Integer state){
        this.orderId = orderId;
        this.couponId = couponId;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
        this.state = state;

    }
}
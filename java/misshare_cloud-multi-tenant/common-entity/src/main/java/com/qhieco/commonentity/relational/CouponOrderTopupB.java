package com.qhieco.commonentity.relational;

import lombok.Data;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 充值订单优惠券
 */
@Data
public class CouponOrderTopupB {
    private Integer id;

    private Integer orderId;

    private Integer couponId;

    private Long createTime;

    private Long modifyTime;

    private Integer state;
}
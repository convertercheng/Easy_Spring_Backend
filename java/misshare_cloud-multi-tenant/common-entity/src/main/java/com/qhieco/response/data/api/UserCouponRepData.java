package com.qhieco.response.data.api;

import java.math.BigDecimal;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 11:18
 * <p>
 * 类说明：
 * ${说明}
 */
@lombok.Data
public class UserCouponRepData {
    /**
     * 用户代金券id，对应coupon表的id
     */
    private Integer userCouponId;
    /**
     * 代金券类型
     */
    private String couponType;
    /**
     * 代金券额度，通过优惠卷类型来区分为百分比或者金额
     */
    private BigDecimal couponLimit;
    /**
     * 代金券失效时间
     */
    private Long endTime;

    /**
     * 卡券编号
     */
    private String couponCode;
}

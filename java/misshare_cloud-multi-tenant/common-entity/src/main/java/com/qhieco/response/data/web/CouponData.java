package com.qhieco.response.data.web;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/3/29 9:18
 * <p>
 * 类说明：
 * 优惠券返回结果类
 */
@Data
public class CouponData {
    /**
     * 优惠券编码
     */
    private String couponCode;
    /**
     * 优惠券密码
     */
    private String couponPassword;
    /**
     * 生效时间
     */
    private Long beginTime;
    /**
     * 失效时间
     */
    private Long endTime;
    /**
     * 优惠券金额
     */
    private BigDecimal couponLimit;
    /**
     * 优惠类型
     */
    private Integer couponType;

    /**
     * 实际使用优惠券金额
     */
    private BigDecimal usedMoney;

    /**
     * 创建时间
     */
    private  Long createTime;

    /**
     * 使用人号码
     */
    private String  userPhone;

    /**
     * 使用时间
     */
    private Long usedTime;

    /**
     * 状态
     */
    private Integer state;

    private String stateStr;

    private String couponTypeStr;
}

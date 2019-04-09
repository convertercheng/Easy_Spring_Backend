package com.qhieco.request.web;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/3/29 9:43
 * <p>
 * 类说明：
 * 优惠券绑定类
 */
@Data
public class CouponRequest extends  QueryPaged{
    /**
     * 优惠券编码
     */
    private String couponCode;
    /**
     * 生效时间
     */
    private Long beginTime;
    /**
     * 失效时间
     */
    private Long endTime;
    /**
     * 优惠券最小金额
     */
    private BigDecimal minCouponLimit;

    /**
     * 优惠券最大金额
     */
    private BigDecimal maxCouponLimit;
    /**
     * 优惠类型
     */
    private Integer couponType;

    /**
     * 实际使用优惠券最小金额
     */
    private BigDecimal minUsedMoney;

    /**
     * 实际使用优惠券最大金额
     */
    private BigDecimal maxUsedMoney;

    /**
     * 创建起始时间
     */
    private  Long startCreateTime;

    /**
     * 创建结束时间
     */
    private  Long endCreateTime;

    /**
     * 使用人号码
     */
    private String  userPhone;

    /**
     * 使用起始时间
     */
    private Long startUsedTime;

    /**
     * 使用结束时间
     */
    private Long endUsedTime;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 优惠券数量
     */
    private  Integer couponNum;

    /**
     * 优惠金额
     */
    private  BigDecimal couponLimit;

    /**
     * 标签集合
     */
    private List<Integer> tagList=new ArrayList<Integer>();

    /**
     * 用户集合
     */
    private List<Integer> userIdList=new ArrayList<Integer>();

    /**
     * 密码
     */
    private String couponPassword;

    /**
     * 优惠金额
     */
    private BigDecimal usedMoney;

    /**
     * 操作类型1-代表指定派发0-代表商家生成
     */
    private Integer operationType;

    /**
     * 商家ID
     */
    private Integer businessId;

    /**
     * 商家名称
     */
    private String businessName;

    /**
     * 时间类型
     */
    private String timeType;
}

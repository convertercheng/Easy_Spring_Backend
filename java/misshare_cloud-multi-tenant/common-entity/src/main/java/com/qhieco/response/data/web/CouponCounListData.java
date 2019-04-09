package com.qhieco.response.data.web;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/6 18:28
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class CouponCounListData {

    private BigDecimal couponLimit;

    private BigDecimal usedMoney;

    private Integer couponType;

}

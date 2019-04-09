package com.qhieco.response.data.web;

import lombok.Data;

import java.util.List;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/6 18:27
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class CouponCountData {

    private Long createTime;

    private List<CouponCounListData> couponCounListData;


}

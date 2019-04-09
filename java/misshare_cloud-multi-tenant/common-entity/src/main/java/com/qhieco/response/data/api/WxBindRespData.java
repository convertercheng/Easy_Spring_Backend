package com.qhieco.response.data.api;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/26 13:20
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class WxBindRespData {
    private Integer orderId;
    private Integer parklotId;
    private String unionId;
    private String phone;
    private Integer userId;
    private BigDecimal totalFee;
    private Long sysTemTime;
    private Integer orderState;
}

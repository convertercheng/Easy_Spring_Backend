package com.qhieco.response.data.web;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import java.math.BigDecimal;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-30 上午10:47
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class OrderRefundData {
    private Integer id;

    private String phone;

    private String tradeNo;

    private BigDecimal fee;

    private Integer state;

    private Long createTime;

    private Integer channel;

    private String stateStr;

    private String channelStr;
}

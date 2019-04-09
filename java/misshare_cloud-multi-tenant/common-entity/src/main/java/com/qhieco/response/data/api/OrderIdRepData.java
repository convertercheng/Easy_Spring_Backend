package com.qhieco.response.data.api;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/14 17:19
 * <p>
 * 类说明：
 * 订单Id返回类
 */
@Data
public class OrderIdRepData {
    Integer orderId;
    BigDecimal totalFee;
}

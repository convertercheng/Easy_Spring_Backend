package com.qhieco.trafficmanage.entity.request;

import lombok.Data;

/**
 * @author 郑旭 790573267@qq.com
 * @version 2.0.1 创建时间: 18-6-13  上午10:12
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class PaymentMessage{
    //支付方式
    private String ZFFS;
    //支付方式描述
    private String ZFFSMS;
    //支付方流水号
    private String ZFFLSH;
    //支付金额
    private String ZFJE;
}

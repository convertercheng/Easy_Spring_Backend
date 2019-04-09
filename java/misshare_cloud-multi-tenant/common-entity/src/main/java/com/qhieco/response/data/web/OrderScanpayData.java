package com.qhieco.response.data.web;

import com.qhieco.commonentity.OrderParkingScanpay;
import lombok.Data;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/6/5 16:28
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class OrderScanpayData extends OrderParkingScanpay {

    private String parklotName;

    private Integer parklotType;

    private Integer parklotKind;

}

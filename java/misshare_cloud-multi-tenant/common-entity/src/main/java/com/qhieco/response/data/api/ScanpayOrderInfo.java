package com.qhieco.response.data.api;

import lombok.Data;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/6/29 19:01
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class ScanpayOrderInfo {

    private Integer parklocId;

    private Integer orderId;

    private Integer orderState;

    private Integer parklotId;

    private Integer mobileUserId;
}

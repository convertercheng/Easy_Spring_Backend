package com.qhieco.request.api;

import lombok.Data;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/5/21 16:17
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class ParkingInfoRequest extends AbstractRequest {
    private String unionId;
    private String plateNo;
    private Integer parklotId;
    private String openId;
}

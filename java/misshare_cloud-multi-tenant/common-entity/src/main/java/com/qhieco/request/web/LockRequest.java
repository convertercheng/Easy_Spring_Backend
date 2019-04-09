package com.qhieco.request.web;

import lombok.Data;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-16 下午4:08
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class LockRequest extends QueryPaged{
    private String parklotName;
    private String serialNumber;
    private Integer state;
    private String battery;
    private String phone;
    private String userName;
    private Integer batteryType;
    private Double batteryNum;
    private String name;
    private Integer type;
}

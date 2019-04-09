package com.qhieco.request.web;

import lombok.Data;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/6/5 16:16
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class OrderScanpayRequest extends QueryPaged {
    private Integer id;
    private String serialNumber;
    private String unionId;
    private String parklotName;
    private String plateNo;
    private Integer payChannel;
    private Integer state;
    private Integer parklotType;
    private Long startCreateTime;
    private Long endCreateTime;
}

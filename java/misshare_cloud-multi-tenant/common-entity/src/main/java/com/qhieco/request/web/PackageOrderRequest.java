package com.qhieco.request.web;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/7/13 14:45
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class PackageOrderRequest extends QueryPaged {

    /**
     * 基础信息
     */
    private Integer id;
    private Integer packageId;
    private String serialNumber;
    private Integer mobileUserId;
    private BigDecimal totalFee;
    private BigDecimal discountFee;
    private BigDecimal realFee;
    private Integer payChannel;
    private Integer invoiceState;
    private String tradeNo;
    private Long payTime;
    private Integer state;
    private Long createTime;
    private Integer userPackageId;

    private Long beginTime; //下单时间-开始时间
    private Long endTime;//下单时间-结束时间

    private String name;//套餐名称
    private String type;//套餐类型
    private String number;//车牌号
    private String phone;//手机号码

}

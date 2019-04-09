package com.qhieco.response.data.web;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/7/13 14:46
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class PackageOrderData {

    /**
     * 基本信息
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

    private String name;
    private String couponCode;//优惠卷编码
    private Integer inNumber; //剩余天数
    private String number; //车牌号
    private String phone; //购买用户


}

package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/7/13 14:40
 * <p>
 * 类说明：
 *
 */
@Data
@Entity
@Table(name = "t_package_order")
public class PackageOrder {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "package_id")
    private Integer packageId;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "mobile_user_id")
    private Integer mobileUserId;

    @Column(name = "total_fee")
    private BigDecimal totalFee;

    @Column(name = "discount_fee")
    private BigDecimal discountFee;

    @Column(name = "real_fee")
    private BigDecimal realFee;

    @Column(name = "pay_channel")
    private Integer payChannel;

    @Column(name = "invoice_state")
    private Integer invoiceState;

    @Column(name = "trade_no")
    private String tradeNo;

    @Column(name = "pay_time")
    private Long payTime;

    @Column(name = "state")
    private Integer state;

    @Column(name = "create_time")
    private Long createTime;

    @Column(name = "user_package_id")
    private Integer userPackageId;

}
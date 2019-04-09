package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/5/22 8:36
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
@Entity
@Table(name = "t_order_parking_scanpay")
public class OrderParkingScanpay {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "union_id")
    private String unionId;

    @Column(name = "parklot_id")
    private Integer parklotId;

    @Column(name = "plate_no")
    private String plateNo;

    @Column(name = "real_start_time")
    private Long realStartTime;

    @Column(name = "real_end_time")
    private Long realEndTime;

    @Column(name = "total_fee")
    private BigDecimal totalFee;

    @Column(name = "discount_fee")
    private BigDecimal discountFee;

    @Column(name = "real_fee")
    private BigDecimal realFee;

    @Column(name = "tripartite_fee")
    private BigDecimal tripartiteFee;

    @Column(name = "pay_channel")
    private Integer payChannel;

    @Column(name = "trade_no")
    private String tradeNo;

    @Column(name = "create_time")
    private Long createTime;

    @Column(name = "pay_time")
    private Long payTime;

    @Column(name = "cancel_time")
    private Long cancelTime;

    @Column(name = "state")
    private Integer state;

    /**
     * 道闸方对应的订单id
     */
    @Column(name = "extract_order_id")
    private String extractOrderId;

    public OrderParkingScanpay() {
    }

    public OrderParkingScanpay(String serialNumber, String unionId, Integer parklotId, String plateNo,
                               Long realStartTime, Long realEndTime, BigDecimal totalFee, BigDecimal discountFee,
                               BigDecimal realFee, Integer payChannel,
                               Long createTime, Integer state, String extractOrderId, BigDecimal tripartiteFee) {
        this.serialNumber = serialNumber;
        this.unionId = unionId;
        this.parklotId = parklotId;
        this.plateNo = plateNo;
        this.realStartTime = realStartTime;
        this.realEndTime = realEndTime;
        this.totalFee = totalFee;
        this.discountFee = discountFee;
        this.realFee = realFee;
        this.payChannel = payChannel;
        this.createTime = createTime;
        this.state = state;
        this.extractOrderId = extractOrderId;
        this.tripartiteFee = tripartiteFee;
    }
}

package com.qhieco.response.data.web;

import com.qhieco.commonentity.OrderParking;
import com.qhieco.commonentity.Reservation;
import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author 许家钰 xujiayu0837@163.com
 * @version 2.0.1 创建时间: 18/3/7 下午6:03
 *          <p>
 *          类说明：
 */
@Data
public class OrderData {

    /**
     * 基础信息
     */
    private Integer id;
    private String serialNumber;
    private String parklotName;
    private String plateNumber;
    private String phone;
    private String parkingNumber;
    private Long createTime;
    private Integer state;

    private Finance finance;

    private Reservation reservation;

    private Parking parking;

    public OrderData(){
        this.reservation = new Reservation();
        this.parking = new Parking();
        this.finance = new Finance();
    }

    /**
     * 预约订单相关信息
     */
    @Data
    public class Reservation{
        private Long startTime;
        private Long endTime;
        private String serialNumber;
        private BigDecimal totalFee;
        private BigDecimal realFee;
        private BigDecimal discountFee;
        private String payChannelStr;
        private String tradeNo;
        private String couponCodes;
        private BigDecimal tripartiteFee;
    }

    /**
     * 停车订单相关信息
     */
    @Data
    public class Parking{
        private Long realStartTime;
        private Long realEndTime;
        private BigDecimal totalFee;
        private BigDecimal realFee;
        private BigDecimal overtimeFee;
        private BigDecimal discountFee;
        private String payChannelStr;
        private String tradeNo;
        private String couponCodes;
        private BigDecimal tripartiteFee;
        private BigDecimal parkingFee;
    }

    /**
     * 财务信息
     */
    @Data
    public class Finance{
        private BigDecimal totalFee;
        private BigDecimal discountFee;
        private BigDecimal realFee;
        private BigDecimal tripartiteFee;
        private BigDecimal platformIncome;
        private BigDecimal ownerIncome;
        private BigDecimal manageIncome;
    }
}

package com.qhieco.response.data.web;

import com.qhieco.commonentity.OrderParking;

import java.math.BigDecimal;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/3/28 11:10
 * <p>
 * 类说明：
 * ${description}
 */
@lombok.Data
public class OrderParkingData{
   private Integer id;
   private Integer reserveId;
   private String serialNumber;
   private Integer mobileUserId;
   private Integer reservationId;
   private Integer parklocId;
   private Long realStartTime;
   private Long realEndTime;
   private BigDecimal totalFee;
   private BigDecimal discountFee;
   private BigDecimal realFee;
   private BigDecimal tripartiteFee;
   private Integer payChannel;
   private Integer invoiceState;
   private String tradeNo;
   private BigDecimal platformIncome;
   private BigDecimal ownerIncome;
   private BigDecimal manageIncome;
   private Long createTime;
   private Long payTime;
   private Long cancelTime;
   private Long overtime;
   private Long overtimeFee;
   private Integer state;
   private String phone;
   private Long startTime;
   private Long endTime;
   private String plateNumber;
   private String parkingNumber;
   private String parkingPhone;
   private String couponCodes;
   private String parklotName;
   private Integer parklotType;
   private Integer parklotId;
}

package com.qhieco.response.data.api;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018-06-22 10:57
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class OrderParkingData {
    private BigDecimal totalFee=BigDecimal.ZERO;
    private Long overtime;
    private BigDecimal overtimeFee=BigDecimal.ZERO;



    private Integer id;
    private Integer reserveId;
    private String serialNumber;
    private Integer mobileUserId;
    private Integer reservationId;
    private Integer parklocId;
    private Long realStartTime;
    private Long realEndTime;
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

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public Long getOvertime() {
        return overtime;
    }

    public void setOvertime(Long overtime) {
        this.overtime = overtime;
    }

    public BigDecimal getOvertimeFee() {
        return overtimeFee;
    }

    public void setOvertimeFee(BigDecimal overtimeFee) {
        this.overtimeFee = overtimeFee;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReserveId() {
        return reserveId;
    }

    public void setReserveId(Integer reserveId) {
        this.reserveId = reserveId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getMobileUserId() {
        return mobileUserId;
    }

    public void setMobileUserId(Integer mobileUserId) {
        this.mobileUserId = mobileUserId;
    }

    public Integer getReservationId() {
        return reservationId;
    }

    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
    }

    public Integer getParklocId() {
        return parklocId;
    }

    public void setParklocId(Integer parklocId) {
        this.parklocId = parklocId;
    }

    public Long getRealStartTime() {
        return realStartTime;
    }

    public void setRealStartTime(Long realStartTime) {
        this.realStartTime = realStartTime;
    }

    public Long getRealEndTime() {
        return realEndTime;
    }

    public void setRealEndTime(Long realEndTime) {
        this.realEndTime = realEndTime;
    }

    public BigDecimal getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(BigDecimal discountFee) {
        this.discountFee = discountFee;
    }

    public BigDecimal getRealFee() {
        return realFee;
    }

    public void setRealFee(BigDecimal realFee) {
        this.realFee = realFee;
    }

    public BigDecimal getTripartiteFee() {
        return tripartiteFee;
    }

    public void setTripartiteFee(BigDecimal tripartiteFee) {
        this.tripartiteFee = tripartiteFee;
    }

    public Integer getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(Integer payChannel) {
        this.payChannel = payChannel;
    }

    public Integer getInvoiceState() {
        return invoiceState;
    }

    public void setInvoiceState(Integer invoiceState) {
        this.invoiceState = invoiceState;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public BigDecimal getPlatformIncome() {
        return platformIncome;
    }

    public void setPlatformIncome(BigDecimal platformIncome) {
        this.platformIncome = platformIncome;
    }

    public BigDecimal getOwnerIncome() {
        return ownerIncome;
    }

    public void setOwnerIncome(BigDecimal ownerIncome) {
        this.ownerIncome = ownerIncome;
    }

    public BigDecimal getManageIncome() {
        return manageIncome;
    }

    public void setManageIncome(BigDecimal manageIncome) {
        this.manageIncome = manageIncome;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getPayTime() {
        return payTime;
    }

    public void setPayTime(Long payTime) {
        this.payTime = payTime;
    }

    public Long getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Long cancelTime) {
        this.cancelTime = cancelTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getParkingNumber() {
        return parkingNumber;
    }

    public void setParkingNumber(String parkingNumber) {
        this.parkingNumber = parkingNumber;
    }

    public String getParkingPhone() {
        return parkingPhone;
    }

    public void setParkingPhone(String parkingPhone) {
        this.parkingPhone = parkingPhone;
    }

    public String getCouponCodes() {
        return couponCodes;
    }

    public void setCouponCodes(String couponCodes) {
        this.couponCodes = couponCodes;
    }

    public String getParklotName() {
        return parklotName;
    }

    public void setParklotName(String parklotName) {
        this.parklotName = parklotName;
    }

    public Integer getParklotType() {
        return parklotType;
    }

    public void setParklotType(Integer parklotType) {
        this.parklotType = parklotType;
    }

    public Integer getParklotId() {
        return parklotId;
    }

    public void setParklotId(Integer parklotId) {
        this.parklotId = parklotId;
    }
}

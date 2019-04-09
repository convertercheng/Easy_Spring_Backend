package com.qhieco.commonentity;

import com.qhieco.TenantSupport;
import com.qhieco.constant.Constants;
import lombok.Data;
import org.hibernate.annotations.Filter;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应停车订单表
 */
@Data
@Entity
@Table(name = "t_order_parking")
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class OrderParking implements TenantSupport{

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "reserve_id")
    private Integer reserveId;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "mobile_user_id")
    private Integer mobileUserId;

    @Column(name = "reservation_id", nullable = false)
    private Integer reservationId;

    @Column(name = "parkloc_id")
    private Integer parklocId;

    @Column(name = "parklot_id")
    private Integer parklotId;

    @Column(name = "plate_id")
    private Integer plateId;

    @Column(name = "real_start_time")
    private Long realStartTime;

    @Column(name = "real_end_time")
    private Long realEndTime;

    @Column(name = "total_fee")
    private BigDecimal totalFee;

    @Column(name = "parking_fee_paid_flag")
    private Integer parkingFeePaidFlag;

    @Column(name = "discount_fee")
    private BigDecimal discountFee;

    @Column(name = "real_fee")
    private BigDecimal realFee;

    @Column(name = "tripartite_fee")
    private BigDecimal tripartiteFee;

    @Column(name = "pay_channel")
    private Integer payChannel;

    @Column(name = "invoice_state")
    private Integer invoiceState;

    @Column(name = "trade_no")
    private String tradeNo;

    @Column(name = "platform_income")
    private BigDecimal platformIncome;

    @Column(name = "owner_income")
    private BigDecimal ownerIncome;

    @Column(name = "manage_income")
    private BigDecimal manageIncome;

    @Column(name = "create_time")
    private Long createTime;

    @Column(name = "pay_time")
    private Long payTime;

    @Column(name = "cancel_time")
    private Long cancelTime;

    @Column(name = "overtime")
    private Long overtime;

    @Column(name = "overtime_fee")
    private BigDecimal overtimeFee;

    @Column(name = "third_party_no")
    private String thirdPartyNo;

    @Column(name = "unique_id")
    private String uniqueId;

    @Column(name = "state")
    private Integer state;

    @Column
    private Integer tenantId;

    // 业务属性，非数据库字段
    /**
     * 车主手机号
     */
    @Transient
    private String phone;

    /**
     * 预约开始时间
     */
    @Transient
    private Long startTime;

    /**
     * 预约结束时间
     */
    @Transient
    private Long endTime;

    /**
     * 车位编号
     */
    @Transient
    private String parkingNumber;

    /**
     * 车位管理员手机号
     */
    @Transient
    private String parkingPhone;

    /**
     * 优惠券编码
     */
    @Transient
    private String couponCodes;

    /**
     * 小区名称
     */
    @Transient
    private String parklotName;

    /**
     * 小区类型
     */
    @Transient
    private Integer parklotType;

    /**
     * 车牌号
     */
    @Transient
    private String plateNumber;

    /**
     * 预约费
     */
    @Transient
    private BigDecimal reservationFee;

    /**
     * 正常停车费
     */
    @Transient
    private BigDecimal parkingFee;

    /**
     * 停车时长
     */
    @Transient
    private String stoptimeStr;

    /**
     * 超时时长
     */
    @Transient
    private String overtimeStr;

    /**
     * 支付渠道
     */
    @Transient
    private String payChannelStr;

    @Transient
    private Integer innershare;

    @Transient
    private String innershareStr;

    @Transient
    private Integer chargeType;

    @Transient
    private String chargeTypeStr;

    @Transient
    private String ovteTimes;

    @Transient
    private String tenantName;

    public OrderParking(){}

    public OrderParking(String serialNumber,Integer mobileUserId,Integer reservationId,Integer parklocId,Integer parklotId,Integer plateId,BigDecimal totalFee,Long createTime,Integer state){
        this.serialNumber = serialNumber;
        this.mobileUserId = mobileUserId;
        this.reservationId = reservationId;
        this.parklocId = parklocId;
        this.parklotId = parklotId;
        this.plateId = plateId;
        this.totalFee = totalFee;
        this.createTime = createTime;
        this.state = state;
        //金额默认值0
        this.discountFee = Constants.BIGDECIMAL_ZERO;
        this.realFee = Constants.BIGDECIMAL_ZERO;
        this.tripartiteFee = Constants.BIGDECIMAL_ZERO;
        this.platformIncome = Constants.BIGDECIMAL_ZERO;
        this.ownerIncome = Constants.BIGDECIMAL_ZERO;
        this.manageIncome = Constants.BIGDECIMAL_ZERO;
        this.overtimeFee = Constants.BIGDECIMAL_ZERO;
    }
    public OrderParking(String serialNumber,Integer mobileUserId,Integer reservationId,Integer parklocId,Integer parklotId,Integer plateId,BigDecimal totalFee,Long createTime,Integer state,Integer payChannel){
        this.serialNumber = serialNumber;
        this.mobileUserId = mobileUserId;
        this.reservationId = reservationId;
        this.parklocId = parklocId;
        this.parklotId = parklotId;
        this.plateId = plateId;
        this.totalFee = totalFee;
        this.createTime = createTime;
        this.state = state;
        this.payChannel=payChannel;
        //金额默认值0
        this.discountFee = Constants.BIGDECIMAL_ZERO;
        this.realFee = Constants.BIGDECIMAL_ZERO;
        this.tripartiteFee = Constants.BIGDECIMAL_ZERO;
        this.platformIncome = Constants.BIGDECIMAL_ZERO;
        this.ownerIncome = Constants.BIGDECIMAL_ZERO;
        this.manageIncome = Constants.BIGDECIMAL_ZERO;
        this.overtimeFee = Constants.BIGDECIMAL_ZERO;
    }

    @Transient
    private String stateStr;
    @Transient
    private String parklotTypeStr;

    public void handelReserve(OrderParking reserve){
        discountFee = discountFee.add(reserve.getDiscountFee());
        tripartiteFee =  tripartiteFee.add(reserve.getTripartiteFee());
        totalFee = totalFee.add(reserve.getTotalFee());
        realFee = realFee.add(reserve.getRealFee());
        platformIncome = platformIncome.add(reserve.getPlatformIncome());
        ownerIncome = ownerIncome.add(reserve.getOwnerIncome());
        manageIncome = manageIncome.add(reserve.getManageIncome());
    }
}
package com.qhieco.response.data.api;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/12 13:41
 * <p>
 * 类说明：
 * 预约/停车订单详情
 */
@Data
public class ReserveOrderDetailRespData {
    private Integer orderId;
    // 公共参数
    /**
     * 车场id
     */
    private Integer parklotId;
    /**
     * 车场名称
     */
    private String parklotName;
    /**
     * 车位编号
     */
    private String parklocNumber;
    /**
     * 车牌号
     */
    private String plateNo;
    /**
     * 车场地址
     */
    private String address;
    /**
     * 类型，0:室内，1:室外，2:室内+室外
     */
    private Integer parklotKind;
    /**
     * 停车区类型，0是静态车场车位查询，1是约车场，2是约车位
     */
    private Integer type;
    /**
     * 计费类型（0是道闸计费，1是地锁计费）
     */
    private Integer chargeType;
    /**
     * 预约进场时间
     */
    private Long startTime;
    /**
     * 预约离场时间
     */
    private Long endTime;
    /**
     * 免费取消预约的提前时间
     */
    private String freeCancellationTime;
    /**
     * 商户订单号
     */
    private String serialNumber;
    /**
     * 订单状态
     */
    private Integer state;
    /**
     * 如果该预约订单已进入停车状态，则返回停车状态
     */
    private Integer parkingState;
    /**
     * 如果该预约订单已进入停车状态，则返回停车订单id
     */
    private Integer orderParkingId;
    /**
     * 订单创建时间
     */
    private Long createTime;
    /**
     * 订单总额
     */
    private BigDecimal totalFee;
    // end

    /**
     * 状态进行中，约车场 的特殊回参 start
      */
    /**
     * 纬度
     */
    private Double lat;
    /**
     * 经度
     */
    private Double lng;
    // end

    /**
     * 状态进行中，约车位 的特殊回参 start
     */
    /**
     *  门禁列表
     */
    private List<AccessRespData> accessList;
    /**
     * 车位锁id
     */
    private Integer lockId;
    /**
     * 车位锁蓝牙名称
     */
    private String lockBtName;
    /**
     * 车位锁蓝牙密码
     */
    private String lockBtPwd;
    /**
     *  车位锁类型 0 : zigbee锁，1: nb锁
     */
    private Integer lockType;
    /**
     * 进入停车倒计时， 预约的开始时间-当前时间
     */
    private Long enterCountdownTime;
    /**
     * 停车时长， 当前时间-实际开始停车时间
     */
    private Long stopTime;
    /**
     * 停车收费规则
     */
    private ParkingFeeData parkingFeeData;
    // end

    /**
     * 状态未支付|已完成|取消|超时取消 的公共回参 start
     */

    /**
     * 实际入场时间
     */
    private Long enterTime;
    /**
     * 实际离场时间
     */
    private Long leaveTime;
    /**
     * 超时时间
     */
    private Long overTime;
    /**
     * 预约费
     */
    private BigDecimal reserveFee;
    /**
     * 停车费
     */
    private BigDecimal parkingFee;
    /**
     * 超时费
     */
    private BigDecimal overTimeFee;
    /**
     * 共享车位被使用，最短计费时间
     */
    private Integer minChargingPeriod;
    /**
     * 需要支付的停车费的金额
     */
    private BigDecimal payFee;
    // end

    /**
     * 状态已完成、取消、超时取消 的特殊回参 start
     */
    /**
     * 停车费 支付渠道，微信或者支付宝
     */
    private Integer payChannelParking;
    /**
     *  线上三方支付停车费金额
     */
    private BigDecimal parkingFeeOnLine;
    /**
     *  线下支付停车费金额
     */
    private BigDecimal parkingFeeOffLine;
    /**
     * 预约费  支付渠道，微信或者支付宝
     */
    private Integer payChannelReserve;
    /**
     *  预约支付优惠券面额
     */
    private BigDecimal reserveCouponFee;
    /**
     * 预约支付优惠券编号
     */
    private String reserveCouponCode;
    /**
     *  停车支付优惠券面额
     */
    private BigDecimal parkingCouponFee;
    /**
     * 停车支付优惠券编号
     */
    private String parkingCouponCode;
    /**
     * 停车支付第三方流水号
     */
    private String parkingTradeNo;
    /**
     * 预约支付第三方流水号
     */
    private String reserveTradeNo;
    // end
}

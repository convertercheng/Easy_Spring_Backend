package com.qhieco.response.data.api;

import lombok.Data;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 14:25
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class ReserveEnterRepData {
    /**
     * 车场id
     */
    private Integer parklotId;
    /**
     * 车场名称
     */
    private String parklotName;
    /**
     * 车场类型，0:室内，1:室外，2:室内+室外
     */
    private Integer parklotKind;
    /**
     * 停车区类型，0是静态车场车位查询，1是约车场，2是约车位
     */
    private Integer type;
    /**
     * 停车场地址
     */
    private String address;
    /**
     * 车位编号
     */
    private String parklocNumber;
    /**
     * 收费规则
     */
    private String feeIntro;
    /**
     * 空泊位数：当前时间可预约的车位数
     */
    private Integer reservableAmount;
    /**
     * 车牌号
     * 只有一个车牌时，默认显示此车牌号码。
     * 有多个车牌时，默认显示最近一次预约过的车牌。车牌均未预约过的，显示最近添加的车牌
     */
    private String plateNo;

    /**
     * 车牌id
     */
    private Integer plateId;
    /**
     * 在预约开始的Free_Cancellation_Time分钟之内，用户可以免费取消预约
     */
    private Integer freeCancellationTime;
    /**
     * 已到入场时间但车主未入场，为车主保留车位的时长。
     */
    private Integer maxDelayTime;
    /**
     * 车位可以提前多长时间预约
     */
    private Integer advanceReservationTime;
    /**
     * 车位可以预约到的最长时间点
     */
    private Long advanceReservationTimePoint;
    /**
     * 系统时间
     */
    private Long systemTime;

    /**
     * 信用系数
     */
    private Double integralPermissionsCoefficient;

    /**
     * 如果用户存在未结束的订单，则返回订单id
     */
    private Integer orderId;


    /**
     * 预约费
     */
    private List<ReserveFeeData> feeList;

    private List<ReserveTimeData> reserveTimeList;

}

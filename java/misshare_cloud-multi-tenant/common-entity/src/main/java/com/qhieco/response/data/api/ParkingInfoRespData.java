package com.qhieco.response.data.api;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/5/21 16:20
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class ParkingInfoRespData {
    /**
     * 车牌号
     */
    private String plateNo;
    /**
     * 用户unionId
     */
    private String unionId;
    /**
     * 用户openId
     */
    private String openId;
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
     * 停车费
     */
    private BigDecimal fee;
    /**
     * 进场时间
     */
    private Long enterTime;
    /**
     * 停车时长
     */
    private Long stopTime;
    /**
     * 免费时长倒计时
     */
    private Long freeTime;
    /**
     * 支付时间
     */
    private Long payTime;

    private String result;

    private String message;
}

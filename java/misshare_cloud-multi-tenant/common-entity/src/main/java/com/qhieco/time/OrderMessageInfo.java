package com.qhieco.time;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/4/6 17:44
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class OrderMessageInfo {
    /**
     * userId
     */
    private Integer userId;
    /**
     * 极光Id
     */
    private String jpushId;
    /**
     * 停车场名称
     */
    private String parklotName;
    /**
     * 停车位编号
     */
    private String parklocNumber;
    /**
     * 预约开始时间
     */
    private Long startTime;
    /**
     * 预约结束时间
     */
    private Long endTime;

}

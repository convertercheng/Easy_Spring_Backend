package com.qhieco.response.data.api;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/2/28 17:27
 * <p>
 * 类说明：
 * 用户正使用订单信息
 */
@lombok.Data
public class OrderUsingRepData {
    /**
     * 订单id
     */
    private Integer orderId;
    /**
     * 实际开始时间
     */
    private Long realStartTime;
    /**
     * 预约的开始时间
     */
    private Long startTime;
    /**
     * 现在距离开始停车间隔时间差
     */
    private Long stopTime;
    /**
     * 订单状态
     */
    private Integer state;

    /**
     * 门禁列表
     */
    private List<AccessRespData> accessList;
}

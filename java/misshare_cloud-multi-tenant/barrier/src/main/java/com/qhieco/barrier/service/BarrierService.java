package com.qhieco.barrier.service;

import com.qhieco.commonentity.OrderParking;
import com.qhieco.request.api.BarrierInfoRequest;
import com.qhieco.request.api.PostCarInfoRequest;
import com.qhieco.response.Resp;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/4/22 下午10:08
 * <p>
 * 类说明：
 *     道闸服务层
 */

public interface BarrierService {
    /**
     * 处理道闸厂商通知的信息
     * @return
     */
    Resp process(BarrierInfoRequest barrierInfoRequest);

    public BigDecimal calculateParkingTotalFee(Long realEndTime, OrderParking parkingOrder);

    /**
     * 处理keyTop入场通知的信息
     * @param request
     * @return
     */
    Resp postCarInInfo(PostCarInfoRequest request);

    /**
     * 处理keyTop出场通知的信息
     * @param request
     * @return
     */
    Resp postCarOutInfo(PostCarInfoRequest request);

    Resp getFee();
}

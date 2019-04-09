package com.qhieco.lock.service;

import com.qhieco.request.api.*;
import com.qhieco.response.Resp;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/28 上午11:04
 * <p>
 * 类说明：
 *     车位锁服务层
 */

public interface LockService {

    /**
     * 控制车锁
     * @param lockControlRequest
     * @return
     */
    Resp control(LockControlRequest lockControlRequest);

    /**
     * 批量控制车锁
     * @param lockBatchControlRequest
     * @return
     */
    Resp batchControl(LockBatchControlRequest lockBatchControlRequest);

    /**
     * 查询本次控制车位状态
     * @param lockGetStateRequest
     * @return
     */
    Resp getState(LockGetStateRequest lockGetStateRequest);

    /**
     * 订单60秒钟不能取消预约
     * @param orderIdRequest
     * @return
     */
    Resp waitTime(OrderIdRequest orderIdRequest);

}

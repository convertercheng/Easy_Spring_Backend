package com.qhieco.lock.service;

import com.qhieco.request.api.LockBatchControlRequest;
import com.qhieco.request.api.LockControlRequest;
import com.qhieco.response.Resp;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/28 上午11:04
 * <p>
 * 类说明：
 *     车位锁服务层
 */

public interface NBLOTService {

    /**
     * 控制车锁
     * @param lockControlRequest
     * @return
     */
    Resp control(LockControlRequest lockControlRequest);

    Resp batchControl(LockBatchControlRequest lockBatchControlRequest);
}

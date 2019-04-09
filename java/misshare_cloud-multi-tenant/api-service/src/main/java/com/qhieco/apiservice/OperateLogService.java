package com.qhieco.apiservice;

import com.qhieco.commonentity.LogRequestApp;
import com.qhieco.commonentity.LogRequestWeb;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/22 下午8:37
 * <p>
 * 类说明：
 *     关于操作日志的Service
 */

public interface OperateLogService {

    /**
     * 存储app request日志
     * @param logRequestApp app请求日志
     */
    void saveLog(LogRequestApp logRequestApp);

}

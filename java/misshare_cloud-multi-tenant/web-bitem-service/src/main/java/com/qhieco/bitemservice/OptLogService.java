package com.qhieco.bitemservice;

import com.qhieco.commonentity.LogRequestWeb;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/22 下午8:37
 * <p>
 * 类说明：
 *     关于操作日志的Service
 */

public interface OptLogService {

    /**
     * 存储web request日志
     * @param logWebRequest logWebRequest
     */
    void saveLog(LogRequestWeb logWebRequest);

}

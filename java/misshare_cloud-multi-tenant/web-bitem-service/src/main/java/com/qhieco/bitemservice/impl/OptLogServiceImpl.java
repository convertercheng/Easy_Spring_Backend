package com.qhieco.bitemservice.impl;

import com.qhieco.bitemservice.OptLogService;
import com.qhieco.commonentity.LogRequestWeb;
import com.qhieco.commonrepo.LogRequestWebRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/22 下午8:37
 * <p>
 * 类说明：
 *     http请求日志的service实现类
 */
@Service
public class OptLogServiceImpl implements OptLogService {

    @Autowired
    private LogRequestWebRepository logWebRequestRepository;

    @Override
    public void saveLog(LogRequestWeb logWebRequest) {
        logWebRequestRepository.save(logWebRequest);
    }
}

package com.qhieco.apiservice.impl;

import com.qhieco.apiservice.OperateLogService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.commonentity.LogRequestApp;
import com.qhieco.commonrepo.LogRequestAppRepository;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/22 下午8:37
 * <p>
 * 类说明：
 *     http请求日志的service实现类
 */
@Service
public class OperateLogServiceImpl implements OperateLogService {

    @Autowired
    private LogRequestAppRepository logRequestAppRepository;

    @Override
    public void saveLog(LogRequestApp logRequestApp) {
        String httpMethod = logRequestApp.getMethod();
        String uri = logRequestApp.getUri();
        List<LogRequestApp> logRequestApps = logRequestAppRepository.findByMethodAndUri(httpMethod,uri);
        //如果存在，做更新操作
        if(null != logRequestApps && logRequestApps.size() > Constants.EMPTY_CAPACITY){
            LogRequestApp resLogRequestApp = logRequestApps.get(Constants.FIRST_INDEX);
            resLogRequestApp.setTimes(resLogRequestApp.getTimes() + logRequestApp.getTimes());
            resLogRequestApp.setSuccessTimes(resLogRequestApp.getSuccessTimes() + logRequestApp.getSuccessTimes());
            logRequestApp = resLogRequestApp;
        }
        if (null == logRequestAppRepository.save(logRequestApp)) {
            throw new QhieException(Status.ApiErr.INSERT_ERROR);
        }
    }
}

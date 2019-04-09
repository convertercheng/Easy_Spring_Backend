package com.qhieco.apiservice.impl;

import com.qhieco.apiservice.LogService;
import com.qhieco.commonentity.LogOperationMobile;
import com.qhieco.commonrepo.LogOperationMobileRepository;
import com.qhieco.request.api.LogRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/17 17:26
 * <p>
 * 类说明：
 * ${说明}
 */
@Service
@Slf4j
public class LogServiceImpl implements LogService {

    @Autowired
    private LogOperationMobileRepository logOperationMobileRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveLog(LogRequest request) {
        LogOperationMobile logOperationMobile = new LogOperationMobile();
        logOperationMobile.setMobileUserId(request.getUser_id());
        logOperationMobile.setOperateTime(System.currentTimeMillis());
        logOperationMobile.setSourceIp(request.getSource_ip());
        logOperationMobile.setType(request.getType());
        logOperationMobile.setSourceModel(request.getSource_model());
        logOperationMobile.setContent(request.getContent());
        logOperationMobileRepository.save(logOperationMobile);
        log.info("保存用户操作日志，" + logOperationMobile);
    }
}

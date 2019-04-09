package com.qhieco.apiwrite.web;

import com.qhieco.apiservice.LogService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.constant.Status;
import com.qhieco.request.api.LogRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.CommonUtil;
import com.qhieco.util.RespUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/17 17:16
 * <p>
 * 类说明：
 * ${说明}
 */
@RestController
@RequestMapping(value = "log")
@Slf4j
public class LogWeb {
    @Autowired
    private LogService logService;

    @PostMapping(value = "save")
    public Resp save(@RequestBody LogRequest request) {
        if (CommonUtil.isTimeStampInValid(request.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        if (StringUtils.isEmpty(request.getUser_id()) || StringUtils.isEmpty(request.getContent()) || StringUtils.isEmpty(request.getSource_ip())
                || StringUtils.isEmpty(request.getSource_model()) || StringUtils.isEmpty(request.getType())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        logService.saveLog(request);
        return RespUtil.successResp();
    }
}

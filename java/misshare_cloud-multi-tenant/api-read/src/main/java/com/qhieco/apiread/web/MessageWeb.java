package com.qhieco.apiread.web;

import com.qhieco.apiservice.MessageService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.constant.Status;
import com.qhieco.request.api.MessageQueryRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.CommonUtil;
import com.qhieco.util.RespUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/14 13:59
 * <p>
 * 类说明：
 * 消息模块controller
 */
@RestController
@RequestMapping(value = "message")
public class MessageWeb {
    @Autowired
    private MessageService messageService;

    /**
     * 用户消息列表
     *
     * @param request
     * @return
     */
    @PostMapping(value = "list")
    public Resp messageList(@RequestBody MessageQueryRequest request) {
        if (CommonUtil.isTimeStampInValid(request.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        if (request.getUser_id() == null || request.getPage_num() == null || request.getPage_num() < 0 || request.getType() == null) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return RespUtil.successResp(messageService.queryMessageListByUserId(request.getUser_id(), request.getPage_num(), request.getType()));
    }
}

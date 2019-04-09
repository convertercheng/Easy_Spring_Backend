package com.qhieco.apiread.web;

import com.qhieco.apiservice.WithdrawService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.constant.Status;
import com.qhieco.request.api.WithdrawRecordQueryRequest;
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
 * @version 2.0.1 创建时间: 2018/3/14 10:40
 * <p>
 * 类说明：
 * ${说明}
 */
@RestController
@RequestMapping(value = "withdraw")
public class WithdrawWeb {

    @Autowired
    private WithdrawService withdrawService;

    @PostMapping(value = "/record/list")
    public Resp withdrawRecordList(@RequestBody WithdrawRecordQueryRequest request) {
        if (CommonUtil.isTimeStampInValid(request.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        if (request.getUser_id() == null || request.getPage_num() == null) {
            throw new QhieException(Status.ApiErr.PARAMS_WITHDRAW_RECORD_QUERY);
        }
        return RespUtil.successResp(withdrawService.queryWithdrawRecordList(request.getUser_id(), request.getPage_num()));
    }
}

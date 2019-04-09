package com.qhieco.apiwrite.web;

import com.qhieco.apiservice.PublishService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.api.PublishBatchAddRequest;
import com.qhieco.request.api.PublishCancelRequest;
import com.qhieco.request.api.PublishAddRequest;
import com.qhieco.request.api.PublishListAlterRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.ParamCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/7 10:34
 * <p>
 * 类说明：
 *       发布控制类
 */
@RestController
@RequestMapping("publish")
public class PublishWeb {

    @Autowired
    PublishService publishService;

    @PostMapping("add")
    public Resp addPublish(@RequestBody PublishAddRequest publishAddRequest) {
        Resp resp = ParamCheck.check(publishAddRequest,  "parkloc_id","user_id","parklot_id","start_time","end_time","mode");
        if (!resp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return publishService.addPublish(publishAddRequest);
    }


    @PostMapping("batchAdd")
    public Resp batchAdd(@RequestBody PublishBatchAddRequest publishBatchAddRequest) {
        Resp resp = ParamCheck.check(publishBatchAddRequest,  "parklocIds","user_id","parklot_id","start_time","end_time","mode");
        if (!resp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        if (Constants.LOOP_MODE.equals(publishBatchAddRequest.getMode()) && StringUtils.isEmpty(publishBatchAddRequest.getDay_of_week())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return publishService.batchAdd(publishBatchAddRequest);
    }

    @PostMapping("cancel")
    public Resp cancel(@RequestBody PublishCancelRequest publishCancelRequest) {
        Resp resp = ParamCheck.check(publishCancelRequest,  "publishIds");
        if (!resp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return publishService.cancel(publishCancelRequest);
    }

    @PostMapping("alter")
    public Resp alter(@RequestBody PublishListAlterRequest publishListAlterRequest) {
        //验证参数完整
        Resp resp = ParamCheck.check(publishListAlterRequest,  "publishIds","parklot_id","start_time","end_time","mode");
        if (!resp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        if (Constants.LOOP_MODE.equals(publishListAlterRequest.getMode()) && StringUtils.isEmpty(publishListAlterRequest.getDay_of_week())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return publishService.alter(publishListAlterRequest);
    }



}

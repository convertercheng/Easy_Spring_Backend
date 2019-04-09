package com.qhieco.barrier.web;

import com.qhieco.barrier.exception.QhieException;
import com.qhieco.barrier.service.BarrierService;
import com.qhieco.constant.Status;
import com.qhieco.request.api.BarrierInfoRequest;
import com.qhieco.request.api.LockControlRequest;
import com.qhieco.request.api.PostCarInfoRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.ParamCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/4/22 下午10:06
 * <p>
 * 类说明：
 *     道闸对接控制层
 */
@RestController
public class BarrierWeb {

    @Autowired
    private BarrierService barrierService;

    @PostMapping("info/notify")
    public Resp getBarrierInfo(@RequestBody BarrierInfoRequest barrierInfoRequest) {
        Resp resp = ParamCheck.check(barrierInfoRequest,  "parklot_id","tag", "license");
        if (!resp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return barrierService.process(barrierInfoRequest);
    }

    @PostMapping("keyTop/postCarInInfo")
    public Resp postCarInInfo(@RequestBody PostCarInfoRequest postCarInfoRequest) {
        Resp resp = ParamCheck.check(postCarInfoRequest,  "appId","key", "plateNo","enteyTime","parkId");
        if (!resp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return barrierService.postCarInInfo(postCarInfoRequest);
    }

    @PostMapping("keyTop/postCarOutInfo")
    public Resp postCarOutInfo(@RequestBody PostCarInfoRequest postCarInfoRequest) {
        Resp resp = ParamCheck.check(postCarInfoRequest,  "appId","key", "plateNo","leaveTime","parkId");
        if (!resp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return barrierService.postCarOutInfo(postCarInfoRequest);
    }

    @GetMapping("getFee")
    public Resp getFee(){
        return barrierService.getFee();
    }
}

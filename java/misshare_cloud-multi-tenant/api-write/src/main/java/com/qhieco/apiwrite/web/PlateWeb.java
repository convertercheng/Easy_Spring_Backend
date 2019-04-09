package com.qhieco.apiwrite.web;

import com.qhieco.apiservice.PlateService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.constant.Status;
import com.qhieco.request.api.PlateAddRequest;
import com.qhieco.request.api.PlateDelRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.ParamCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/7 17:46
 * <p>
 * 类说明：
 * ${desription}
 */
@RestController
@RequestMapping("plate")
public class PlateWeb {

    @Autowired
    private PlateService plateService;

    @PostMapping("add")
    public Resp addPlate(@RequestBody PlateAddRequest plateAddRequest) {
        Resp resp = ParamCheck.check(plateAddRequest,  "user_id","plate_num");
        if (!resp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return plateService.addPlate(plateAddRequest);
    }

    @PostMapping("del")
    public Resp delPlate(@RequestBody PlateDelRequest plateDelRequest) {
        Resp resp = ParamCheck.check(plateDelRequest, "user_id", "plate_id");
        if (!resp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return plateService.delPlate(plateDelRequest);
    }

}

package com.qhieco.apiwrite.web;

import com.qhieco.apiservice.ParklocService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.constant.Status;
import com.qhieco.request.api.ParklocAddRequest;
import com.qhieco.request.api.ParklotUsualSetRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.ParamCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/5 10:34
 * <p>
 * 类说明：
 *       车位控制类
 */
@RestController
@RequestMapping("parkloc")
public class ParklocWeb {

    @Autowired
    ParklocService parklocService;

    @PostMapping("/add")
    public Resp addParkloc(@RequestBody ParklocAddRequest parklocAddRequest) {
        Resp resp = ParamCheck.check(parklocAddRequest,  "user_id","area_id","parklot_name","contact_phone");
        if (!resp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return parklocService.addParkloc(parklocAddRequest);
    }

}

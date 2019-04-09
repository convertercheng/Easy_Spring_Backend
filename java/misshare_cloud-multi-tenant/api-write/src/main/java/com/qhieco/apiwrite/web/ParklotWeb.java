package com.qhieco.apiwrite.web;

import com.qhieco.apiservice.OrderService;
import com.qhieco.apiservice.ParklotService;
import com.qhieco.apiservice.exception.ParamException;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.barrier.epaitc.request.EnterRequest;
import com.qhieco.constant.Status;
import com.qhieco.request.api.ParklotUsualSetRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.ParamCheck;
import com.qhieco.util.RespUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/26 10:34
 * <p>
 * 类说明：
 *       停车场控制类
 */
@RestController
@RequestMapping("parklot")
@Slf4j
public class ParklotWeb {

    @Autowired
    private ParklotService parklotService;

    @Autowired
    private OrderService orderService;

    @PostMapping("/usual/set")
    public Resp setUsual(@RequestBody ParklotUsualSetRequest parklotUsualSetRequest) {
        Resp resp = ParamCheck.check(parklotUsualSetRequest,  "user_id","parklot_id");
        if (!resp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return parklotService.setUsual(parklotUsualSetRequest);
    }

    /*@PostMapping("info/get")
    public Resp getParklotInfo(@RequestBody EnterRequest enterRequest) {
        log.info("enterquest is {}", enterRequest);
        Resp checkResp = ParamCheck.check(enterRequest, "park_id", "tag", "license");
        if (! checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.ApiErr.PARAMS_ERROR.getCode());
        }
        orderService.updateOrderStatus(enterRequest);
        return RespUtil.successResp();
    }*/

}

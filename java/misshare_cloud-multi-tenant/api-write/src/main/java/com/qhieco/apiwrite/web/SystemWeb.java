package com.qhieco.apiwrite.web;

import com.qhieco.apiservice.SystemService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.constant.Status;
import com.qhieco.request.api.VerificationCodeGetRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.api.VerificationCodeGetRepData;
import com.qhieco.util.ParamCheck;
import com.qhieco.util.RespUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.qhieco.constant.Status.ApiErr.PARAMS_VERIFICATION_ERROR;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/2/24 下午4:33
 * <p>
 * 类说明：
 *     系统控制类
 */
@RestController
@RequestMapping("system")
public class SystemWeb {

    @Autowired
    private SystemService systemService;

    @PostMapping("verification/code/get")
    public Resp getVerificationCode(@RequestBody VerificationCodeGetRequest verificationCodeGetRequest) {
        Resp checkResp = ParamCheck.check(verificationCodeGetRequest, "phone", "timestamp", "anti");
        if (! checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        VerificationCodeGetRepData verificationCodeGetRepData =  systemService.getVerificationCode(verificationCodeGetRequest);
        return RespUtil.successResp(verificationCodeGetRepData);
    }
}

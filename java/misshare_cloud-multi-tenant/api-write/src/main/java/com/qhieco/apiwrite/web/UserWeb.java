package com.qhieco.apiwrite.web;

import com.qhieco.apiservice.UserService;
import com.qhieco.apiservice.exception.ParamException;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.constant.Status;
import com.qhieco.request.api.UserLoginRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.api.UserLoginRepData;
import com.qhieco.util.CusAccessObjectUtil;
import com.qhieco.util.ParamCheck;
import com.qhieco.util.RespUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/2/23 下午2:55
 * <p>
 * 类说明：
 *     用户控制类
 */
@RestController
@RequestMapping("user")
public class UserWeb {

    @Autowired
    private UserService userService;

    /**
     * 用户登录注册接口
     */
    @PostMapping(value = "login")
    public Resp userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        Resp checkResp = ParamCheck.check(userLoginRequest, "phone", "jpush_id","timestamp");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.ApiErr.PARAMS_ERROR.getCode());
        }
        userLoginRequest.setIp(CusAccessObjectUtil.getIpAddress(request));
        UserLoginRepData userLoginRepData = userService.login(userLoginRequest);
        return RespUtil.successResp(userLoginRepData);
    }

    @PostMapping("avatar/upload")
    public Resp uploadAvatar(@RequestParam("file") MultipartFile file, @RequestParam("user_id") Integer userId, @RequestParam("timestamp") String timestamp) {
        if (null == file || null == userId || StringUtils.isEmpty(timestamp)) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return userService.uploadAvatar(file, userId, timestamp);
    }
}

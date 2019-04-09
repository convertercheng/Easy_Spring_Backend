package com.qhieco.apiread.web;

import com.qhieco.apiservice.UserService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.constant.Status;
import com.qhieco.request.api.UserPersonCenterRequest;
import com.qhieco.request.api.UserWithdrawAmountRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.CommonUtil;
import com.qhieco.util.RespUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 14:47
 * <p>
 * 类说明：
 * ${说明}
 */
@RestController
@RequestMapping(value = "user")
@Slf4j
public class UserWeb {
    @Autowired
    private UserService userService;

    /**
     * 个人中心，获取用户类型和卡券数量等信息
     *
     * @param request
     * @return
     */
    @PostMapping("info/get")
    public Resp queryPersonCenterInfo(@RequestBody UserPersonCenterRequest request) {
        if (CommonUtil.isTimeStampInValid(request.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        if (request.getUser_id() == null) {
            throw new QhieException(Status.ApiErr.PARAMS_PERSON_CENTER_QUERY);
        }
        return RespUtil.successResp(userService.queryPersonCenterInfo(request.getUser_id()));
    }

    /**
     * 获取用户可提现金额以及银行卡信息
     *
     * @return
     */
    @PostMapping(value = "withdraw/amount")
    public Resp queryUserWithdrawAmount(@RequestBody UserWithdrawAmountRequest request) {
        if (CommonUtil.isTimeStampInValid(request.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        if (request.getUser_id() == null) {
            throw new QhieException(Status.ApiErr.PARAMS_USER_WITHDRAW_QUERY);
        }
        return RespUtil.successResp(userService.queryUserWithdrawAmountByUserId(request.getUser_id()));
    }
}

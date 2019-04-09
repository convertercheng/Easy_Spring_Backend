package com.qhieco.webbitem.controller;

import com.qhieco.bitemservice.UserBItemService;
import com.qhieco.constant.Status;
import com.qhieco.request.web.UserRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.ParamCheck;
import com.qhieco.webservice.exception.ParamException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/7/3 14:00
 * <p>
 * 类说明：
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserBItemWeb {

    @Autowired
    UserBItemService userBItemService;

    /**
     * 修改登录密码
     * @param request
     * @return
     */
    @PostMapping(value = "/updateLoginPass")
    public Resp updateLoginPass(UserRequest request) {
        Resp checkResp = ParamCheck.check(request, "newPass", "checkPass");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.BItemErr.SYSTEM_ERROR.getCode());
        }
        return userBItemService.updateLoginPass(request);
    }

    /**
     * 分页显示企业账户信息列表
     * @param request
     * @return
     */
    @PostMapping(value = "/pagetable")
    public Resp pagetable(UserRequest request) {
        Resp checkResp = ParamCheck.check(request, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.BItemErr.SYSTEM_ERROR.getCode());
        }
        return userBItemService.pageUser(request);
    }

    /**
     * 分页显示子账户信息列表
     * @param request
     * @return
     */
    @PostMapping(value = "/pagetableBySubaccount")
    public Resp pagetableBySubaccount(UserRequest request) {
        Resp checkResp = ParamCheck.check(request, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.BItemErr.SYSTEM_ERROR.getCode());
        }
        return userBItemService.pageUser(request);
    }

    /**
     * 获取账户详情
     * @param id
     * @return
     */
    @PostMapping(value = "/findUser")
    public Resp findUserDetailByUserId(Integer id) {
        return userBItemService.findUserDetailByUserId(id);
    }

    /**
     * 新增/编辑 企业账户信息
     * @param request
     * @return
     */
    @PostMapping(value = "/saveOrUpdate")
    public Resp saveOrUpdate(UserRequest request) {
        Resp checkResp = ParamCheck.check(request, "username", "password", "companyName", "state");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.BItemErr.SYSTEM_ERROR.getCode());
        }
        return userBItemService.saveOrUpdate(request);
    }

    /**
     * 新增/编辑 子账户信息
     * @param request
     * @return
     */
    @PostMapping(value = "/saveOrUpdateBySubaccount")
    public Resp saveOrUpdateBySubaccount(UserRequest request) {
        Resp checkResp = ParamCheck.check(request, "username", "password", "companyName", "state");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.BItemErr.SYSTEM_ERROR.getCode());
        }
        return userBItemService.saveOrUpdateBySubaccount(request);
    }
}

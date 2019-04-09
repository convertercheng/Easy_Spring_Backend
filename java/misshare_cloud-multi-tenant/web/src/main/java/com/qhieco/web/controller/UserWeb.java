package com.qhieco.web.controller;

import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.web.UserRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.UserData;
import com.qhieco.util.ExcelUtil;
import com.qhieco.util.ParamCheck;
import com.qhieco.util.RespUtil;
import com.qhieco.webservice.CouponService;
import com.qhieco.webservice.UserService;
import com.qhieco.webservice.exception.ParamException;
import com.qhieco.webservice.exception.QhieWebException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/3/30 19:42
 * <p>
 * 类说明：
 *
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserWeb {

    @Autowired
    UserService userService;

    /**
     * 用户列表导出
     * @param response
     * @param userRequest
     */
    @RequestMapping(value = "/excel")
    public void excel(HttpServletResponse response, UserRequest userRequest){
        if (ExcelUtil.paramCount(userRequest) == Constants.EMPTY_CAPACITY) {
            throw new QhieWebException(Status.WebErr.EMPTY_EXCEL_PARAM);
        }
        // 下载文件的默认名称
        try {
            userService.excel(userRequest,response.getOutputStream(), UserData.class);
            response.setHeader("Content-Disposition",
                    "attachment;filename="+ new String(("用户列表" + ".xls").getBytes(), "iso-8859-1"));
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 分页显示用户信息列表
     * @param userRequest
     * @return
     */
    @PostMapping(value = "/pageable")
    public Resp all(UserRequest userRequest){
        Resp checkResp = ParamCheck.check(userRequest, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return  userService.pageUser(userRequest);
    }

    /**
     * 分页显示用户基础信息列表
     * @param userRequest
     * @return
     */
    @PostMapping(value = "/pageableByAccount")
    public Resp pageableByAccount(UserRequest userRequest){
        Resp checkResp = ParamCheck.check(userRequest, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return  userService.pageUserDetailed(userRequest);
    }


    /**
     * 查看用户详情
     * @param id
     * @return 用户对象
     */
    @GetMapping(value = "/{id}")
    public Resp one(@PathVariable Integer id){
        if(id == null) {
            throw new QhieWebException(Status.WebErr.EMPTY_INPUT_PARAM);
        }
        return userService.findUserOne(id);
    }


    /**
     * 编辑用户身份证和姓名
     * @param userRequest
     * @return
     */
    @PostMapping(value = "/edit")
    public Resp edit(UserRequest userRequest){
        Resp checkResp = ParamCheck.check(userRequest, "id","name","identityNumber");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return userService.editUser(userRequest);
    }

    /**
     * 解除用户绑定
     * @param id
     * @return
     */
    @GetMapping(value = "/untie/{id}")
    public Resp untieWxUser(@PathVariable Integer id){
        if(id==null){
            throw new QhieWebException(Status.WebErr.EMPTY_INPUT_PARAM);
        }
        return userService.untieWxUser(id);
    }

}

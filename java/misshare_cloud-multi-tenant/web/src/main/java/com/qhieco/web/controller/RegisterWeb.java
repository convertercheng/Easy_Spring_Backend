package com.qhieco.web.controller;

import com.qhieco.commonentity.Register;
import com.qhieco.commonentity.relational.UserRegisterB;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.web.RegisterRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.ParamCheck;
import com.qhieco.webservice.RegisterService;
import com.qhieco.webservice.exception.ParamException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/12 18:18
 * <p>
 * 类说明：
 * 标记来源控制层
 */
@RestController
@RequestMapping("/register")
@Slf4j
public class RegisterWeb {

    @Autowired
    private RegisterService registerService;

    /**
     * 注册来源标记分页列表
     *
     * @param registerRequest
     * @return
     */
    @PostMapping(value = "/pageable")
    public Resp query(RegisterRequest registerRequest) {
        Resp checkResp = ParamCheck.check(registerRequest, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return registerService.query(registerRequest);
    }

    /**
     * 保存注册来源标记
     * @param register
     * @return
     */
    @PostMapping(value = "/save")
    public Resp save(Register register){
        register.setPv(0);
        register.setUv(0);
        register.setState(Status.Common.VALID.getInt());
        return registerService.saveUpdate(register);
    }

    /**
     * 修改注册来源标记PV或UV的统计量
     * @param id
     * @return
     */
    @GetMapping(value = "eidt/{id}")
    public Resp eidtPv(@PathVariable  Integer id){
       return  registerService.eidt(id, Constants.USER_REGISTERONE);
    }

    /**
     * 绑定或修改关系
     * @param userRegisterB
     * @return
     */
    @PostMapping(value = "/saveUserRegister")
    public Resp saveUserRegister(UserRegisterB userRegisterB){
        return registerService.saveUpdateUserRegisterB(userRegisterB);
    }

    /**
     * 查询注册来源标记详情
     * @param id
     * @return
     */
    @GetMapping(value = "one/{id}")
    public Resp findOne(@PathVariable  Integer id){
        return registerService.findOne(id);
    }

    /**
     * 编辑注册来源标记
     * @param register
     * @return
     */
    @PostMapping(value="eidtRegister")
    public Resp eidtRegister(Register register){
        return  registerService.eidtRegister(register);
    }
}

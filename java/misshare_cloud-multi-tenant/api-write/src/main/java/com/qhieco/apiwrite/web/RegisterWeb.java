package com.qhieco.apiwrite.web;

import com.qhieco.apiservice.RegisterService;
import com.qhieco.commonentity.Register;
import com.qhieco.commonentity.relational.UserRegisterB;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.api.RegisterRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.ParamCheck;
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


    @PostMapping(value = "/edit")
    public Resp editPv(@RequestBody  RegisterRequest registerRequest){
       return  registerService.edit(registerRequest);
    }

    @PostMapping(value = "/saveUserRegister")
    public Resp saveUserRegister(@RequestBody  RegisterRequest registerRequest){
        return registerService.saveUpdateUserRegisterB(registerRequest);
    }
}

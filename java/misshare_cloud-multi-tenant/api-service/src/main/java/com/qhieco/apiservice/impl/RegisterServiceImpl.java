package com.qhieco.apiservice.impl;

import com.qhieco.apiservice.RegisterService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.commonentity.Register;
import com.qhieco.commonentity.relational.UserRegisterB;
import com.qhieco.commonrepo.RegisterRepository;
import com.qhieco.commonrepo.UserRegisterBRepository;
import com.qhieco.constant.Status;
import com.qhieco.request.api.RegisterRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.CommonUtil;
import com.qhieco.util.RespUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/12 18:10
 * <p>
 * 类说明：
 * ${description}
 */

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private RegisterRepository registerRepository;

    @Autowired
    private UserRegisterBRepository userRegisterBRepository;

    @Override
    public Resp edit(RegisterRequest registerRequest) {
        //验证时间戳是否合法
        if (CommonUtil.isTimeStampInValid(registerRequest.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        if (registerRequest.getId()==null) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        Register register = registerRepository.findOne(registerRequest.getId());
        if (register != null) {
            register.setPv(register.getPv() + 1);
            registerRepository.save(register);
        }
        return RespUtil.successResp();
    }

    @Override
    public Resp saveUpdateUserRegisterB(RegisterRequest registerRequest) {
        if (CommonUtil.isTimeStampInValid(registerRequest.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
       UserRegisterB userRegisterB1= userRegisterBRepository.findUserRegisterBByIdentification(
               registerRequest.getRegisterId(),registerRequest.getIdentification(),Status.Common.VALID.getInt());
       if(userRegisterB1==null && StringUtils.isNotEmpty(registerRequest.getIdentification()) && registerRequest.getRegisterId()!=null){
           userRegisterB1=new UserRegisterB();
           userRegisterB1.setIdentification(registerRequest.getIdentification());
           userRegisterB1.setRegisterId(registerRequest.getRegisterId());
           userRegisterB1.setState(Status.Common.VALID.getInt());
           userRegisterB1.setUpdateTime(System.currentTimeMillis());
           userRegisterBRepository.save(userRegisterB1);
       }
        return RespUtil.successResp();
    }
}

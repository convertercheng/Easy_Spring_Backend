package com.qhieco.webservice.impl;

import com.qhieco.commonentity.Register;
import com.qhieco.commonentity.relational.UserRegisterB;
import com.qhieco.commonrepo.RegisterRepository;
import com.qhieco.commonrepo.UserRegisterBRepository;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.web.RegisterRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.BeanUtil;
import com.qhieco.util.PageableUtil;
import com.qhieco.util.RespUtil;
import com.qhieco.webservice.RegisterService;
import com.qhieco.webservice.exception.QhieWebException;
import com.qhieco.webservice.utils.QueryFunction;
import lombok.val;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

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

    private QueryFunction<Register, RegisterRequest> queryFunction;

    @PostConstruct
    public void init() {
        queryFunction = new QueryFunction<>(registerRepository);
        queryFunction.setTransientProperty(this::transientProperty);
    }

    private Register transientProperty(Register data) {
        val newData = new Register();
        BeanUtil.converJavaBean(data, newData);
        return newData;
    }

    @Override
    public Resp query(RegisterRequest registerRequest) {
        return queryFunction.query(registerRequest,where(registerRequest));
    }

    @Override
    public Resp saveUpdate(Register register) {
        registerRepository.save(register);
        return RespUtil.successResp();
    }

    private Specification<Register> where(RegisterRequest request) {
        return (root, query, cb) -> {
            val page = new PageableUtil(root, query, cb);
            return page.pridect();
        };
    }

    @Override
    public Resp eidt(Integer id, String type) {
        Register register=registerRepository.findOne(id);
        if(Constants.USER_REGISTERONE.equals(type)){
            register.setPv(register.getPv()+1);
        }else{
            register.setUv(register.getUv()+1);
        }
        registerRepository.save(register);
        return RespUtil.successResp();
    }

    @Override
    public Resp saveUpdateUserRegisterB(UserRegisterB userRegisterB) {
       UserRegisterB userRegisterB1= userRegisterBRepository.findUserRegisterBByIdentification(
                userRegisterB.getRegisterId(),userRegisterB.getIdentification(),Status.Common.VALID.getInt());
       if(userRegisterB1==null){
           userRegisterB1=new UserRegisterB();
           userRegisterB1.setIdentification(userRegisterB.getIdentification());
           userRegisterB1.setRegisterId(userRegisterB.getRegisterId());
           userRegisterB.setState(Status.Common.VALID.getInt());
       }
       if(userRegisterB1!=null && userRegisterB1.getMoblieUserId()==null){
           userRegisterB1.setMoblieUserId(userRegisterB.getMoblieUserId());
           eidt(userRegisterB1.getId(),Constants.USER_REGISTERTWO);
       }
        userRegisterB1.setUpdateTime(System.currentTimeMillis());
        userRegisterBRepository.save(userRegisterB1);
        return RespUtil.successResp();
    }

    @Override
    public Resp findOne(Integer id) {
        return RespUtil.successResp(registerRepository.findOne(id));
    }

    @Override
    public Resp eidtRegister(Register register) {
        if(register!=null && register.getId()!=null){
            throw new QhieWebException(Status.WebErr.EMPTY_INPUT_PARAM);
        }
        Register register1=registerRepository.findOne(register.getId());
        if(StringUtils.isNotEmpty(register.getContent())){
            register1.setContent(register.getContent());
        }
        if(StringUtils.isNotEmpty(register.getKeyword())){
            register1.setKeyword(register.getKeyword());
        }
        if(StringUtils.isNotEmpty(register.getMedium())){
            register1.setMedium(register.getMedium());
        }
        if(StringUtils.isNotEmpty(register.getSeries())){
            register1.setSeries(register.getSeries());
        }
        if(StringUtils.isNotEmpty(register.getSource())){
            register1.setSource(register.getSource());
        }
        if(StringUtils.isNotEmpty(register.getRemark())) {
            register1.setRemark(register.getRemark());
        }
        if(StringUtils.isNotEmpty(register.getFileName())) {
            register1.setFileName(register.getFileName());
        }
        registerRepository.save(register1);
        return RespUtil.successResp();
    }
}

package com.qhieco.web.controller;

import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.web.IntegralDataRequest;
import com.qhieco.request.web.IntegralRequest;
import com.qhieco.request.web.LockRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.ParamCheck;
import com.qhieco.util.RespUtil;
import com.qhieco.webservice.IntegralPermissionsLevelService;
import com.qhieco.webservice.IntegralService;
import com.qhieco.webservice.exception.ParamException;
import com.qhieco.webservice.exception.QhieWebException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/5/24 14:09
 * <p>
 * 类说明：
 * ${description}
 */
@RestController
@RequestMapping("/integral")
@Slf4j
public class IntegralController {

    @Autowired
    private IntegralService integralService;

    @Autowired
    private IntegralPermissionsLevelService integralPermissionsLevelService;

    /**
     * 查询加分或减分积分项列表
     * @param integralRequest
     * @return
     */
    @PostMapping(value = "/all")
    public Resp all(IntegralRequest integralRequest){
        integralRequest.setIntegralType(integralRequest.getIntegralType());
        return integralService.query(integralRequest);
    }

    /**
     * 查询积分权限列表
     * @return
     */
    @PostMapping(value = "/allPermissions")
    public Resp allPermissions(){
        return RespUtil.successResp(integralPermissionsLevelService.findList());
    }


    /**
     * 修改积分项和权限系数
     * @param integralDataRequest
     * @return
     */
    @PostMapping(value = "/saveUpdateIntegral")
    public Resp saveUpdateIntegral(@RequestBody  IntegralDataRequest integralDataRequest){
        if(integralDataRequest.getIntegralRequestList()==null || integralDataRequest.getIntegralRequestList().size()==Constants.EMPTY_CAPACITY){
            throw new QhieWebException(Status.WebErr.PARAM_ERROR);
        }
        return RespUtil.successResp(integralService.saveUpdateIntegral(integralDataRequest.getIntegralRequestList()));
    }
}

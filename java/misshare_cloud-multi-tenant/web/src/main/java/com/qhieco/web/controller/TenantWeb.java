package com.qhieco.web.controller;

import com.qhieco.commonentity.Tenant;
import com.qhieco.constant.Status;
import com.qhieco.request.web.TenantRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.ParamCheck;
import com.qhieco.webservice.TenantService;
import com.qhieco.webservice.exception.QhieWebException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 19-1-10 上午10:25
 * <p>
 * 类说明：
 * ${description}
 */
@Controller
@RequestMapping("/tenant")
@Slf4j
public class TenantWeb {

    @Autowired
    TenantService tenantService;

    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Resp save(Tenant tenant){
        Resp resp = ParamCheck.check(tenant, "name");
        if (!resp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new QhieWebException(Status.WebErr.PARAM_ERROR);
        }
        return tenantService.save(tenant);
    }

    @ResponseBody
    @RequestMapping(value = "/pageable", method = RequestMethod.POST)
    public Resp query(TenantRequest request){
        return tenantService.query(request);
    }

}

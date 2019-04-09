package com.qhieco.web.controller;

import com.qhieco.commonentity.iotdevice.Gateway;
import com.qhieco.constant.Status;
import com.qhieco.request.web.GatewayRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.ExcelUtil;
import com.qhieco.util.ParamCheck;
import com.qhieco.webservice.GatewayService;
import com.qhieco.webservice.exception.ParamException;
import com.qhieco.webservice.exception.QhieWebException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-22 上午10:33
 * <p>
 * 类说明：
 * ${description}
 */
@RestController
@RequestMapping("/gateway")
@Slf4j
public class GatewayWeb {
    @Autowired
    GatewayService gatewayService;

    @RequestMapping(value = "/excel")
    public void excel(HttpServletResponse response, GatewayRequest request){
        if (ExcelUtil.paramCount(request.getParklotName(),request.getIdentifier(),
                request.getName(),request.getRouterName(),request.getRouterNumber()) == 0) {
            throw new QhieWebException(Status.WebErr.EMPTY_EXCEL_PARAM);
        }
        // 下载文件的默认名称
        try {
            gatewayService.excel(request, Gateway.class, response.getOutputStream());
            response.setHeader("Content-Disposition",
                    "attachment;filename="+ new String(("网关" + ".xls").getBytes(), "iso-8859-1"));
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @PostMapping(value = "/pageable")
    public Resp all(GatewayRequest gatewayRequest){
        Resp checkResp = ParamCheck.check(gatewayRequest, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return gatewayService.query(gatewayRequest);
    }

    @GetMapping(value = "/{id}")
    public Resp one(@PathVariable Integer id){
        if(id == null) {
            throw new QhieWebException(Status.WebErr.EMPTY_INPUT_PARAM);
        }
        return gatewayService.one(id);
    }

    @PostMapping(value = "/noRouter")
    public Resp noParkloc(GatewayRequest gatewayRequest){
        return gatewayService.unbindQuery(gatewayRequest);
    }

    @PostMapping(value = "/save")
    public Resp save(Gateway gateway){
        Resp paramResp = ParamCheck.check(gateway, "identifier");
        if(gateway.getId()==null && !paramResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(paramResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return gatewayService.save(gateway);
    }

    @PostMapping(value = "/bind")
    public Resp bind(@RequestParam(value = "gatewayIdList") Integer[] gatewayIds,
                     @RequestParam(value = "routerId") Integer routerId){
        if(gatewayIds == null || gatewayIds.length == 0 || routerId == null){
            throw new QhieWebException(Status.WebErr.EMPTY_INPUT_PARAM);
        }
        return gatewayService.bind(gatewayIds,routerId);
    }

    @PostMapping(value = "/unbind")
    public Resp unbind(@RequestParam(value = "gatewayId") Integer gatewayId){
        if(gatewayId == null){
            throw new QhieWebException(Status.WebErr.EMPTY_INPUT_PARAM);
        }
        return gatewayService.unbind(gatewayId);
    }

    @PostMapping(value = "/noLock")
    public Resp noLock(GatewayRequest gatewayRequest){
        return gatewayService.unbindQuery(gatewayRequest);
    }

}

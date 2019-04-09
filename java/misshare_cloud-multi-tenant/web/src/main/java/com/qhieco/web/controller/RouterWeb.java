package com.qhieco.web.controller;

import com.qhieco.commonentity.iotdevice.Router;
import com.qhieco.constant.Status;
import com.qhieco.request.web.GatewayRequest;
import com.qhieco.request.web.RouterRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.ExcelUtil;
import com.qhieco.util.ParamCheck;
import com.qhieco.webservice.RouterService;
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
 * @version 2.0.1 创建时间: 18-3-23 下午2:38
 * <p>
 * 类说明：
 * ${description}
 */
@RestController
@RequestMapping("/router")
@Slf4j
public class RouterWeb {
    @Autowired
    RouterService routerService;

    @RequestMapping(value = "/excel")
    public void excel(HttpServletResponse response, RouterRequest request){
        if (ExcelUtil.paramCount(request.getParklotName(),request.getManufacturerName(),
                request.getModel(),request.getName()) == 0) {
            throw new QhieWebException(Status.WebErr.EMPTY_EXCEL_PARAM);
        }
        // 下载文件的默认名称
        try {
            routerService.excel(request, Router.class, response.getOutputStream());
            response.setHeader("Content-Disposition",
                    "attachment;filename="+ new String(("路由器" + ".xls").getBytes(), "iso-8859-1"));
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @PostMapping(value = "/pageable")
    public Resp all(RouterRequest routerRequest){
        Resp checkResp = ParamCheck.check(routerRequest, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return routerService.query(routerRequest);
    }

    @PostMapping(value = "/one")
    public Resp one( GatewayRequest request){
        Resp checkResp = ParamCheck.check(request, "sEcho", "start", "length", "routerId");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return routerService.one(request);
    }

    @PostMapping(value = "/noParklot")
    public Resp noParkloc(RouterRequest routerRequest){
        return routerService.unbindQuery(routerRequest);
    }

    @PostMapping(value = "/save")
    public Resp save(Router router){
        Resp paramResp = ParamCheck.check(router, "name");
        if(router.getId()==null && !paramResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(paramResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return routerService.save(router);
    }

    @PostMapping(value = "/bind")
    public Resp bind(@RequestParam(value = "routerIdList") Integer[] routerIds,
                     @RequestParam(value = "parklotId") Integer routerId){
        if(routerIds == null || routerIds.length == 0 || routerId == null){
            throw new QhieWebException(Status.WebErr.EMPTY_INPUT_PARAM);
        }
        return routerService.bind(routerIds,routerId);
    }

    @PostMapping(value = "/unbind")
    public Resp unbind(@RequestParam(value = "routerId") Integer routerId){
        if(routerId == null){
            throw new QhieWebException(Status.WebErr.EMPTY_INPUT_PARAM);
        }
        return routerService.unbind(routerId);
    }

}

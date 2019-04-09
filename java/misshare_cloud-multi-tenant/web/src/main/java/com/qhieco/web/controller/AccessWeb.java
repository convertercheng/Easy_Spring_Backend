package com.qhieco.web.controller;

import com.qhieco.commonentity.iotdevice.Access;
import com.qhieco.constant.Status;
import com.qhieco.request.web.AccessRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.ExcelUtil;
import com.qhieco.util.ParamCheck;
import com.qhieco.webservice.AccessService;
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
 * @version 2.0.1 创建时间: 18-3-15 上午10:32
 * <p>
 * 类说明：
 *     门禁web
 */
@RestController
@RequestMapping("/access")
@Slf4j
public class AccessWeb {
    @Autowired
    AccessService accessService;

    @RequestMapping(value = "/excel")
    public void excel(HttpServletResponse response, AccessRequest request){
        if (ExcelUtil.paramCount(request.getName()) == 0) {
            throw new QhieWebException(Status.WebErr.EMPTY_EXCEL_PARAM);
        }
        // 下载文件的默认名称
        try {
            accessService.excel(request, Access.class, response.getOutputStream());
            response.setHeader("Content-Disposition",
                    "attachment;filename="+ new String(("门禁" + ".xls").getBytes(), "iso-8859-1"));
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @PostMapping(value = "/pageable")
    public Resp all(AccessRequest accessRequest){
        Resp checkResp = ParamCheck.check(accessRequest, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return accessService.query(accessRequest);
    }

    @GetMapping(value = "/{id}")
    public Resp one(@PathVariable Integer id){
        if(id == null) {
            throw new QhieWebException(Status.WebErr.EMPTY_INPUT_PARAM);
        }
        return accessService.one(id);
    }

    @PostMapping(value = "/noParklot")
    public Resp noParklot(AccessRequest accessRequest){
        return accessService.unbindQuery(accessRequest);
    }

    @PostMapping(value = "/save")
    public Resp save(Access access){
        Resp paramResp = ParamCheck.check(access, "name", "btName", "btPwd", "intro");
        if(access.getId()==null && !paramResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(paramResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        access.setCreateTime(System.currentTimeMillis());
        return accessService.save(access);
    }

    @PostMapping(value = "/bind")
    public Resp bind(@RequestParam(value = "accessIdList") Integer[] accessIds,
                     @RequestParam(value = "parklotId") Integer parklotId){
        if(accessIds == null || accessIds.length == 0 || parklotId == null){
            throw new QhieWebException(Status.WebErr.EMPTY_INPUT_PARAM);
        }
        return accessService.bind(accessIds,parklotId);
    }

    @PostMapping(value = "/unbind")
    public Resp unbind(@RequestParam(value = "accessId") Integer accessId){
        if(accessId == null){
            throw new QhieWebException(Status.WebErr.EMPTY_INPUT_PARAM);
        }
        return accessService.unbind(accessId);
    }

}

package com.qhieco.web.controller;

import com.qhieco.commonentity.iotdevice.Relaymeter;
import com.qhieco.constant.Status;
import com.qhieco.request.web.RelaymeterRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.ExcelUtil;
import com.qhieco.util.ParamCheck;
import com.qhieco.util.RespUtil;
import com.qhieco.webservice.RelaymeterService;
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
 * @version 2.0.1 创建时间: 18-3-20 下午3:19
 * <p>
 * 类说明：
 * ${description}
 */
@RestController
@RequestMapping("/relaymeter")
@Slf4j
public class RelaymeterWeb {
    @Autowired
    RelaymeterService relaymeterService;

    @RequestMapping(value = "/excel")
    public void excel(HttpServletResponse response, RelaymeterRequest request){
        if (ExcelUtil.paramCount(request.getParklotName(),request.getNumber(),request.getManufacturerName(),request.getModel()) == 0) {
            throw new QhieWebException(Status.WebErr.EMPTY_EXCEL_PARAM);
        }
        // 下载文件的默认名称
        try {
            relaymeterService.excel(request, Relaymeter.class, response.getOutputStream());
            response.setHeader("Content-Disposition",
                    "attachment;filename="+ new String(("继电器" + ".xls").getBytes(), "iso-8859-1"));
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @PostMapping(value = "/pageable")
    public Resp all(RelaymeterRequest relaymeterRequest){
        Resp checkResp = ParamCheck.check(relaymeterRequest, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return relaymeterService.query(relaymeterRequest);
    }

    @GetMapping(value = "/{id}")
    public Resp one(@PathVariable Integer id){
        if(id == null) {
            throw new QhieWebException(Status.WebErr.EMPTY_INPUT_PARAM);
        }
        return relaymeterService.one(id);
    }

    @PostMapping(value = "/noParklot")
    public Resp noParklot(RelaymeterRequest relaymeterRequest){
        return relaymeterService.unbindQuery(relaymeterRequest);
    }

    @PostMapping(value = "/save")
    public Resp save(Relaymeter relaymeter){
        Resp paramResp = ParamCheck.check(relaymeter, "name", "number", "type");
        if(relaymeter.getId()==null && !paramResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(paramResp, Status.WebErr.PARAM_ERROR.getCode());
        }
        if(relaymeter.getId()!=null && relaymeterService.findRelaymeterByName(relaymeter.getName(),Status.Common.VALID.getInt(),relaymeter.getId())!=null){
            return RespUtil.errorResp(Status.WebErr.RELAYMETER_EXISTS.getCode(),Status.WebErr.RELAYMETER_EXISTS.getMsg());
        }
        if(relaymeter.getId()==null && relaymeterService.findRelaymeterByName(relaymeter.getName(),Status.Common.VALID.getInt())!=null){
            return RespUtil.errorResp(Status.WebErr.RELAYMETER_EXISTS.getCode(),Status.WebErr.RELAYMETER_EXISTS.getMsg());
        }
        return relaymeterService.save(relaymeter);
    }

    @PostMapping(value = "/bind")
    public Resp bind(@RequestParam(value = "relaymeterIdList") Integer[] relaymeterIds,
                     @RequestParam(value = "parklotId") Integer parklotId){
        if(relaymeterIds == null || relaymeterIds.length == 0 || parklotId == null){
            throw new QhieWebException(Status.WebErr.EMPTY_INPUT_PARAM);
        }
        return relaymeterService.bind(relaymeterIds,parklotId);
    }

    @PostMapping(value = "/unbind")
    public Resp unbind(@RequestParam(value = "relaymeterId") Integer relaymeterId){
        if(relaymeterId == null){
            throw new QhieWebException(Status.WebErr.EMPTY_INPUT_PARAM);
        }
        return relaymeterService.unbind(relaymeterId);
    }

}

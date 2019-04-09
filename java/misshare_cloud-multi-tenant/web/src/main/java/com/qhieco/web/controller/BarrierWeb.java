package com.qhieco.web.controller;

import com.qhieco.commonentity.iotdevice.Barrier;
import com.qhieco.constant.Status;
import com.qhieco.request.web.BarrierRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.ExcelUtil;
import com.qhieco.util.ParamCheck;
import com.qhieco.webservice.BarrierService;
import com.qhieco.webservice.exception.ParamException;
import com.qhieco.webservice.exception.QhieWebException;
import com.qhieco.webservice.impl.BarrierServiceImpl;
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
 * @version 2.0.1 创建时间: 18-3-12 下午4:08
 * <p>
 * 类说明：
 * ${description}
 */
@RestController
@RequestMapping("/barrier")
@Slf4j
public class BarrierWeb {

    @Autowired
    BarrierService barrierService;

    @RequestMapping(value = "/excel")
    public void excel(HttpServletResponse response, BarrierRequest barrierRequest){
        if (ExcelUtil.paramCount(barrierRequest.getParklotName(), barrierRequest.getName()) == 0) {
            throw new QhieWebException(Status.WebErr.EMPTY_EXCEL_PARAM);
        }
        // 下载文件的默认名称
        try {
            barrierService.excel(barrierRequest,Barrier.class, response.getOutputStream());
            response.setHeader("Content-Disposition",
                    "attachment;filename="+ new String(("道闸" + ".xls").getBytes(), "iso-8859-1"));
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @PostMapping(value = "/pageable")
    public Resp all(BarrierRequest barrierRequest){
        Resp checkResp = ParamCheck.check(barrierRequest, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return barrierService.query(barrierRequest);
    }

    @GetMapping(value = "/{id}")
    public Resp one(@PathVariable Integer id){
        if(id == null) {
            throw new QhieWebException(Status.WebErr.EMPTY_INPUT_PARAM);
        }
        return barrierService.one(id);
    }

    @PostMapping(value = "/noParklot")
    public Resp noParklot(BarrierRequest barrierRequest){
        return barrierService.unbindQuery(barrierRequest);
    }

    @PostMapping(value = "/save")
    public Resp save(Barrier barrier){
        Resp paramResp = ParamCheck.check(barrier, "name", "password", "serverIp", "serverPort");
        if(barrier.getId() == null && !paramResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(paramResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return barrierService.save(barrier);
    }

    @PostMapping(value = "/bind")
    public Resp bind(@RequestParam(value = "barrierIdList") Integer[] barrierIds,
                     @RequestParam(value = "parklotId") Integer parklotId){
        if(barrierIds == null || barrierIds.length == 0 || parklotId == null){
            throw new QhieWebException(Status.WebErr.EMPTY_INPUT_PARAM);
        }
        return barrierService.bind(barrierIds,parklotId);
    }

    @PostMapping(value = "/unbind")
    public Resp unbind(@RequestParam(value = "barrierId") Integer barrierId){
        if(barrierId == null){
            throw new QhieWebException(Status.WebErr.EMPTY_INPUT_PARAM);
        }
        return barrierService.unbind(barrierId);
    }

    @GetMapping(value = "/estate/{id}")
    public Resp estateBarrier(@PathVariable Integer id) {
        if(id == null){
            throw new QhieWebException(Status.WebErr.EMPTY_INPUT_PARAM);
        }
        return barrierService.estateBarrier(id);
    }

}

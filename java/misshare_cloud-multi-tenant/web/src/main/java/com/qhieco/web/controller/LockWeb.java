package com.qhieco.web.controller;

import com.qhieco.commonentity.iotdevice.Lock;
import com.qhieco.constant.Status;
import com.qhieco.request.web.IdRequest;
import com.qhieco.request.web.LockRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.ExcelUtil;
import com.qhieco.util.ParamCheck;
import com.qhieco.webservice.LockService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-19 下午3:58
 * <p>
 * 类说明：
 * ${description}
 */
@RestController
@RequestMapping("/lock")
@Slf4j
public class LockWeb {
    @Autowired
    LockService lockService;

    @RequestMapping(value = "/excel")
    public void excel(HttpServletResponse response, LockRequest request){
        if (ExcelUtil.paramCount(request.getParklotName(),request.getBattery(),
                request.getSerialNumber(),request.getState()) == 0) {
            throw new QhieWebException(Status.WebErr.EMPTY_EXCEL_PARAM);
        }
        // 下载文件的默认名称
        try {
            lockService.excel(request, Lock.class, response.getOutputStream());
            response.setHeader("Content-Disposition",
                    "attachment;filename="+ new String(("车位锁" + ".xls").getBytes(), "iso-8859-1"));
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @PostMapping(value = "/pageable")
    public Resp all(LockRequest lockRequest){
        Resp checkResp = ParamCheck.check(lockRequest, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return lockService.query(lockRequest);
    }

    @GetMapping(value = "/{id}")
    public Resp one(@PathVariable Integer id){
        if(id == null) {
            throw new QhieWebException(Status.WebErr.EMPTY_INPUT_PARAM);
        }
        return lockService.one(id);
    }

    @PostMapping(value = "/noParkloc")
    public Resp noParkloc(LockRequest lockRequest){
        return lockService.unbindQuery(lockRequest);
    }

    @PostMapping(value = "/save")
    public Resp save(Lock lock){
        Resp paramResp = ParamCheck.check(lock, "name", "btName", "btPassword", "serialNumber", "mac", "battery");
        if(lock.getId()==null && !paramResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(paramResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return lockService.saveLock(lock);
    }

    @PostMapping(value = "/bind/parkloc")
    public Resp bind(@RequestParam(value = "lockIdList") Integer[] lockIds,
                     @RequestParam(value = "parklocId") Integer parklotId){
        if(lockIds == null || lockIds.length == 0 || parklotId == null){
            throw new QhieWebException(Status.WebErr.EMPTY_INPUT_PARAM);
        }
        return lockService.bind(lockIds,parklotId);
    }

    @PostMapping(value = "/unbind/parkloc")
    public Resp unbind(@RequestParam(value = "lockId") Integer lockId){
        if(lockId == null){
            throw new QhieWebException(Status.WebErr.EMPTY_INPUT_PARAM);
        }
        return lockService.unbind(lockId);
    }

    @PostMapping(value = "/log/pageable")
    public Resp logPageable(IdRequest idRequest){
        Resp checkResp = ParamCheck.check(idRequest, "id");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return lockService.logPageable(idRequest);
    }

    @GetMapping(value="/downloadAllQr")
    public void  downloadAllQr(HttpServletRequest httpServletRequest,
                               HttpServletResponse httpServletResponse, @RequestParam("data") String data)throws Exception{
        lockService.downloadAllQr(httpServletRequest,httpServletResponse,data);

    }
}

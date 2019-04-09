package com.qhieco.web.controller;

import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.web.FeekbackRequest;
import com.qhieco.request.web.LogOperationWebRequest;
import com.qhieco.request.web.LoginLogRequest;
import com.qhieco.request.web.QueryPaged;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.AbstractPaged;
import com.qhieco.response.data.web.LogLoginData;
import com.qhieco.util.ExcelUtil;
import com.qhieco.util.ParamCheck;
import com.qhieco.util.RespUtil;
import com.qhieco.webservice.SysManageService;
import com.qhieco.webservice.exception.ParamException;
import com.qhieco.webservice.exception.QhieWebException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/7 10:54
 * <p>
 * 类说明：
 * 系统管理中心模块
 */
@RestController
@RequestMapping(value = "sys/manage")
@Slf4j
public class SysManageWeb {
    @Autowired
    private SysManageService sysManageService;

    /**
     * 意见反馈列表
     *
     * @param request
     * @return
     */
    @PostMapping(value = "feekback/list")
    public Resp feekbackList(FeekbackRequest request) {
        if (request.getStart() == null || request.getLength() == null) {
            throw new QhieWebException(Status.WebErr.PARAM_ERROR);
        }
        return RespUtil.successResp(sysManageService.queryFeekbackList(request.getPhone(), request.getStartTime(), request.getEndTime(),
                request.getProIdList(), request.getStart(), request.getLength(),request.getSEcho()));
    }

    /**
     * 用户操作日志列表
     *
     * @param request
     * @return
     */
    @PostMapping(value = "operation/log/list")
    public Resp userOperateLogList(QueryPaged request) {
        if (request.getStart() == null || request.getLength() == null) {
            throw new QhieWebException(Status.WebErr.PARAM_ERROR);
        }
        return RespUtil.successResp(sysManageService.queryLogOperateList(request.getStart(), request.getLength(),request.getSEcho()));
    }


    @PostMapping(value = "login/log/list")
    public Resp userLoginLog(LoginLogRequest request) {
        if (request.getStart() == null || request.getLength() == null) {
            throw new QhieWebException(Status.WebErr.PARAM_ERROR);
        }
        AbstractPaged<LogLoginData> data=sysManageService.queryLogLoginList(request.getPhone(), request.getPhoneModel(), request.getLoginStartTime(),
                request.getLoginEndTime(), request.getStart(), request.getLength(),request.getSEcho());
        return RespUtil.successResp(data);
    }

    /**
     * 管理员日志列表
     * @param logOperationWebRequest
     * @return
     */
    @PostMapping(value = "operationWeb/log/list")
    public Resp operationWebList(LogOperationWebRequest logOperationWebRequest){
        Resp checkResp = ParamCheck.check(logOperationWebRequest, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return RespUtil.successResp(sysManageService.queryOperationWebList(logOperationWebRequest));
    }

    /**
     * 管理员日志导出
     * @param response
     * @param logOperationWebRequest
     */
    @PostMapping(value = "operationWeb/log/excel")
    public void excel(HttpServletResponse response, LogOperationWebRequest logOperationWebRequest) {
        if (ExcelUtil.paramCount(logOperationWebRequest) == Constants.EMPTY_CAPACITY) {
            throw new QhieWebException(Status.WebErr.EMPTY_EXCEL_PARAM);
        }
        // 下载文件的默认名称
        try {
            sysManageService.operationWebListExcel(logOperationWebRequest, response.getOutputStream());
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(("管理员日志信息" + ".xls").getBytes(), "iso-8859-1"));
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * APP日志导出
     * @param response
     * @param request
     */
    @PostMapping(value = "login/log/excel")
    public void operationWebListExcel(HttpServletResponse response,LoginLogRequest request){
        if (ExcelUtil.paramCount(request) == Constants.EMPTY_CAPACITY) {
            throw new QhieWebException(Status.WebErr.EMPTY_EXCEL_PARAM);
        }
        // 下载文件的默认名称
        try {
            sysManageService.queryLogLoginExcel(request,response.getOutputStream());
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(("APP登录日志信息" + ".xls").getBytes(), "iso-8859-1"));
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 意见反馈列表
     *
     * @param request
     * @return
     */
    @PostMapping(value = "feekback/excel")
    public void feekbackListExcel(HttpServletResponse response,FeekbackRequest request) {
        if (ExcelUtil.paramCount(request) == Constants.EMPTY_CAPACITY) {
            throw new QhieWebException(Status.WebErr.EMPTY_EXCEL_PARAM);
        }
        try {
            sysManageService.queryFeedbackListByConditionExcel(request, response.getOutputStream());
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(("用户反馈信息信息" + ".xls").getBytes(), "iso-8859-1"));
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
        }catch (IOException e){
            log.error(e.getMessage());
        }
    }
}

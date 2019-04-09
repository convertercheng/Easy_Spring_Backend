package com.qhieco.web.controller;

import com.qhieco.constant.Status;
import com.qhieco.request.web.FeedbackRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.ExcelUtil;
import com.qhieco.util.ParamCheck;
import com.qhieco.webservice.ManageService;
import com.qhieco.webservice.exception.ParamException;
import com.qhieco.webservice.exception.QhieWebException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-4-3 下午4:42
 * <p>
 * 类说明：
 * ${description}
 */
@RestController
@Slf4j
public class ManageWeb {
    @Autowired
    ManageService manageService;

    @PostMapping(value = "/feedback/pageable")
    public Resp parkingOrder(FeedbackRequest feedbackRequest) {
        Resp checkResp = ParamCheck.check(feedbackRequest, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return manageService.feedback(feedbackRequest);
    }

    @PostMapping(value = "feedback/excel")
    public void excel(HttpServletResponse response, FeedbackRequest request){
        if (ExcelUtil.paramCount(request.getStartCreateTime(),
                request.getEndCreateTime(),request.getPhone(), request.getProblems()) == 0) {
            throw new QhieWebException(Status.WebErr.EMPTY_EXCEL_PARAM);
        }
        // 下载文件的默认名称
        try {
            manageService.feedbackExcel(request, response.getOutputStream());
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(("Feedback" + ".xls").getBytes(), "iso-8859-1"));
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
        } catch (IOException e) {
            log.error("导出意见反馈的Excel表格异常，" + e);
        }
    }

    @RequestMapping(value = "urls")
    public void urls(HttpServletRequest request){
        WebApplicationContext wc = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
        RequestMappingHandlerMapping rmhp = wc.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = rmhp.getHandlerMethods();
        for(RequestMappingInfo info : map.keySet()){
            System.out.println(info.getPatternsCondition().toString()
                    + ","
                    +map.get(info).getBean().toString());
        }
    }
}

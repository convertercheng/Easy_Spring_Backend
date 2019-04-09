package com.qhieco.webbitem.controller;

import com.qhieco.bitemservice.ReportService;
import com.qhieco.bitemservice.exception.ParamException;
import com.qhieco.constant.Status;
import com.qhieco.request.webbitem.OrderReportRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.ParamCheck;
import com.qhieco.util.RespUtil;
import com.qhieco.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/7/5 13:45
 * <p>
 * 类说明：
 * 收费报表web
 */
@RestController
@RequestMapping(value = "report")
@Slf4j
public class ReportWeb {

    @Autowired
    private ReportService reportService;

    /**
     * 查询收费报表
     *
     * @param request
     * @return
     */
    @PostMapping(value = "query")
    public Resp report(OrderReportRequest request) {
        log.info("请求报表接口参数：" + request);
        try {
            Resp checkResp = ParamCheck.check(request, "parklotId", "startTime", "endTime");
            if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
                throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
            }
            return RespUtil.successResp(reportService.queryReport(request));
        } catch (Exception e) {
            log.error("报表接口异常：" + e);
        }
        return RespUtil.errorResp(Status.WebErr.SYSTEM_ERROR.getCode(), Status.WebErr.SYSTEM_ERROR.getMsg());
    }

    /**
     * 收费统计列表
     *
     * @param request
     * @return
     */

    @PostMapping(value = "list")
    public Resp queryReportList(OrderReportRequest request) {
        log.info("报表统计列表方法参数：" + request);
        Resp checkResp = ParamCheck.check(request, "sEcho", "start", "length", "parklotId", "startTime", "endTime");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return RespUtil.successResp(reportService.queryReportList(request));
    }


    @PostMapping(value = "list/excel")
    public void queryParkingRecordListExcel(OrderReportRequest request, HttpServletResponse response) {
        // 下载文件的默认名称
        try {
            reportService.queryReportListExcel(request, response.getOutputStream());
            String fileName = TimeUtil.timeStamp2Date(request.getStartTime(), "yyyyMMdd") +
                    "-" + TimeUtil.timeStamp2Date(request.getEndTime(), "yyyyMMdd");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String((fileName + ".xls").getBytes(), "iso-8859-1"));
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
        } catch (Exception e) {
            log.error("导出收费报表异常 ， " + e.getMessage());
            response.setStatus(404);
        }
    }
}

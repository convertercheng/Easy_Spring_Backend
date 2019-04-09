package com.qhieco.web.controller;

import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.web.StatisticsRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.StatisticsData;
import com.qhieco.util.ExcelUtil;
import com.qhieco.util.ParamCheck;
import com.qhieco.webservice.StatisticsService;
import com.qhieco.webservice.exception.ParamException;
import com.qhieco.webservice.exception.QhieWebException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/6/13 11:38
 * <p>
 * 类说明：
 * ${说明}
 */
@RestController
@RequestMapping(value = "/statistics")
@Slf4j
public class StatisticsWeb {

    @Autowired
    private StatisticsService statisticsService;

    /**
     * 累计流量统计
     * @param request
     * @return
     */
    @PostMapping(value = "/allData")
    public Resp activityStatisticsData(StatisticsRequest request) {
        return statisticsService.findStatisticsByActivityType(request);
    }

    /**
     * 活动触发统计
     * @param request
     * @return
     */
    @PostMapping(value = "/offCountData")
    public Resp getActivityOffCountData(StatisticsRequest request) {
        return statisticsService.getActivityOffCountData(request);
    }

    /**
     * 活动详情列表
     * @param request
     * @return
     */
    @PostMapping(value = "/getActivityOffCountData")
    public Resp getActivityCountByList(StatisticsRequest request) {
        Resp checkResp = ParamCheck.check(request, "sEcho", "start", "length", "activityId");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return statisticsService.getActivityCountByList(request);
    }

    /**
     * 活动统计列表导出
     * @param request
     * @param response
     */
    @RequestMapping(value = "/excel")
    public void excel(HttpServletResponse response, StatisticsRequest request){
        Resp resp = ParamCheck.check(request,  "activityId", "activityType");
        if (!resp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(resp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        if (ExcelUtil.paramCount(request) == Constants.EMPTY_CAPACITY) {
            throw new QhieWebException(Status.WebErr.EMPTY_EXCEL_PARAM);
        }

        // 下载文件的默认名称
        try {
            statisticsService.excel(request,response.getOutputStream(), StatisticsData.class);
            response.setHeader("Content-Disposition", "attachment;filename="+ new String(("活动统计列表" + ".xls").getBytes(), "iso-8859-1"));
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
        } catch (IOException e) {
            log.error(e.getMessage());
            response.setStatus(404);
        }
    }

}

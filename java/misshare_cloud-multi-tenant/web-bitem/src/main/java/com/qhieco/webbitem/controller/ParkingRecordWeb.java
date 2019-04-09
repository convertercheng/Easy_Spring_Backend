package com.qhieco.webbitem.controller;

import com.qhieco.bitemservice.ParkingRecordService;
import com.qhieco.constant.Status;
import com.qhieco.request.webbitem.ParkingRecordRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.ParamCheck;
import com.qhieco.util.RespUtil;
import com.qhieco.webservice.exception.ParamException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/7/3 15:25
 * <p>
 * 类说明：
 * 车辆进出记录web
 */
@RestController
@Slf4j
@RequestMapping(value = "parking/record")
public class ParkingRecordWeb {

    @Autowired
    private ParkingRecordService parkingRecordService;

    @PostMapping(value = "list")
    public Resp queryParkingRecordList(ParkingRecordRequest request) {
        log.info("查询停车进出场记录参数：request =" + request);

        Resp checkResp = ParamCheck.check(request, "sEcho", "start", "length", "parklotId");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return RespUtil.successResp(parkingRecordService.queryParkingRecordList(request));
    }

    @PostMapping(value = "list/excel")
    public void queryParkingRecordListExcel(ParkingRecordRequest request, HttpServletResponse response) {
        // 下载文件的默认名称
        try {
            parkingRecordService.queryParkingRecordListExcel(request, response.getOutputStream());
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(("车辆进出记录" + ".xls").getBytes(), "iso-8859-1"));
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
        } catch (Exception e) {
            log.error("导出车辆进出记录列表异常 ， " + e.getMessage());
            response.setStatus(404);
        }
    }
}

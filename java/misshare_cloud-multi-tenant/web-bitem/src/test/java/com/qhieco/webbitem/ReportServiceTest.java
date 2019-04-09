package com.qhieco.webbitem;

import com.qhieco.bitemservice.ReportService;
import com.qhieco.request.webbitem.OrderReportRequest;
import com.qhieco.response.data.webbitem.OrderReportTotal;
import com.qhieco.webbitemmapper.ReportMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/7/5 16:23
 * <p>
 * 类说明：
 * ${说明}
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class ReportServiceTest {
    @Autowired
    private ReportService reportService;
    @Autowired
    private ReportMapper reportMapper;
    @Autowired
    private MockHttpServletResponse response;

    @Test
    public void queryReportTest() throws Exception {
        OrderReportRequest request = new OrderReportRequest();
        request.setParklotId(11);
        request.setStartTime(1530403200000L);
        request.setEndTime(1531180800000L);
        OrderReportTotal orderReportTotal = reportService.queryReport(request);

        log.info(" ----- orderReportTotal = " + orderReportTotal);
    }

    @Test
    public void queryReserveReportTest() throws Exception {
        OrderReportRequest request = new OrderReportRequest();
        request.setParklotId(11);
        request.setStartTime(1530403200000L);
        request.setEndTime(1531180800000L);

        log.info(" -----  " + reportMapper.queryReserveReport(request));
    }

    @Test
    public void queryReportListTest() throws Exception {
        OrderReportRequest request = new OrderReportRequest();
        request.setParklotId(11);
        request.setStartTime(1530403200000L);
        request.setEndTime(1531180800000L);
        request.setStart(0);
        request.setLength(1);
        log.info(" -----  " + reportService.queryReportList(request));
    }

    @Test
    public void queryReportListExcelTest() throws Exception{
        OrderReportRequest request = new OrderReportRequest();
        reportService.queryReportListExcel(request, response.getOutputStream());
    }
}

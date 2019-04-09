package com.qhieco.bitemservice;

import com.qhieco.request.webbitem.OrderReportRequest;
import com.qhieco.response.data.web.AbstractPaged;
import com.qhieco.response.data.webbitem.OrderReportList;
import com.qhieco.response.data.webbitem.OrderReportTotal;

import java.io.OutputStream;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/7/5 13:45
 * <p>
 * 类说明：
 * ${说明}
 */
public interface ReportService {

    public OrderReportTotal queryReport(OrderReportRequest request) throws Exception ;

    public AbstractPaged<OrderReportList> queryReportList(OrderReportRequest request);

    public void queryReportListExcel(OrderReportRequest request, OutputStream outputStream) throws Exception;
}

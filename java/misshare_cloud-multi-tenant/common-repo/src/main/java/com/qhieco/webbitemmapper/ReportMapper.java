package com.qhieco.webbitemmapper;

import com.qhieco.request.webbitem.OrderReportRequest;
import com.qhieco.response.data.webbitem.OrderReportData;
import com.qhieco.response.data.webbitem.OrderReportList;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/7/5 13:47
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface ReportMapper {

    public BigDecimal queryPayAmount(OrderReportRequest request);

    public Integer queryParkingNum(OrderReportRequest request);

    public Integer queryReserveNum(OrderReportRequest request);

    public List<OrderReportData> queryReserveReport(OrderReportRequest request);

    public List<OrderReportData> queryParkingReport(OrderReportRequest request);

    public List<OrderReportList> queryReportList(OrderReportRequest request);

    public Integer countReport(OrderReportRequest request);
}

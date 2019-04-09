package com.qhieco.bitemservice.impl;

import com.qhieco.bitemservice.ReportService;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.webbitem.OrderReportRequest;
import com.qhieco.response.data.web.AbstractPaged;
import com.qhieco.response.data.webbitem.OrderReportData;
import com.qhieco.response.data.webbitem.OrderReportList;
import com.qhieco.response.data.webbitem.OrderReportTotal;
import com.qhieco.util.ExcelSheetUtil;
import com.qhieco.util.TimeUtil;
import com.qhieco.webbitemmapper.ReportMapper;
import com.qhieco.webservice.exception.ExcelException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/7/5 13:46
 * <p>
 * 类说明：
 * ${说明}
 */
@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportMapper reportMapper;

    @Override
    public OrderReportTotal queryReport(OrderReportRequest request) throws Exception {
        // 预约订单图:按日期升序
        List<OrderReportData> reserveOrderReportDataList = reportMapper.queryReserveReport(request);
        // 停车订单图：按日期升序
        List<OrderReportData> parkingOrderReportDataList = reportMapper.queryParkingReport(request);

        reserveOrderReportDataList = this.supplementData(reserveOrderReportDataList, request.getStartTime(), request.getEndTime());
        parkingOrderReportDataList = this.supplementData(parkingOrderReportDataList, request.getStartTime(), request.getEndTime());

        OrderReportTotal orderReportTotal = new OrderReportTotal();
        orderReportTotal.setReserveOrderReportList(reserveOrderReportDataList);
        orderReportTotal.setParkingOrderReportList(parkingOrderReportDataList);

        this.getReportTitleInfo(request, orderReportTotal);
        return orderReportTotal;
    }

    public void getReportTitleInfo(OrderReportRequest request, OrderReportTotal orderReportTotal) {
        // 应收金额
        BigDecimal amount = reportMapper.queryPayAmount(request);
        // 预约订单数
        Integer reserveNum = reportMapper.queryReserveNum(request);
        // 停车订单数
        Integer parkingNum = reportMapper.queryParkingNum(request);

        orderReportTotal.setAmount(amount);
        orderReportTotal.setParkingNum(parkingNum);
        orderReportTotal.setReserveNum(reserveNum);
    }

    /**
     * 补充空缺日期的数据
     *
     * @return
     */
    public List<OrderReportData> supplementData(List<OrderReportData> orderReportDataList, Long sdate, Long edate) throws Exception {
        int days = TimeUtil.differentDaysByMillisecond(sdate, edate);

        List<OrderReportData> result = new ArrayList<>(days + 1);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currDate;
        Calendar calendar = Calendar.getInstance();

        int index = 0;
        int len = orderReportDataList.size();
        OrderReportData orderReportData;
        for (int i = 0; i <= days; i++) {
            calendar.setTime(new Date(sdate));
            calendar.add(Calendar.DAY_OF_YEAR, i);
            currDate = sdf.format(calendar.getTime());

            if (index < len) {
                if (currDate.equals(orderReportDataList.get(index).getDayOfDate())) {
                    orderReportData = orderReportDataList.get(index);
                    index++;
                } else {
                    orderReportData = new OrderReportData(currDate, 0, BigDecimal.ZERO);
                }
            } else {
                orderReportData = new OrderReportData(currDate, 0, BigDecimal.ZERO);
            }
            result.add(orderReportData);
        }
        return result;
    }

    @Override
    public AbstractPaged<OrderReportList> queryReportList(OrderReportRequest request) {
        int count = reportMapper.countReport(request);
        List<OrderReportList> orderReportLists = null;
        if (count > 0) {
            orderReportLists = reportMapper.queryReportList(request);
        }

        AbstractPaged<OrderReportList> data = AbstractPaged.<OrderReportList>builder()
                .sEcho(request.getSEcho() == null ? 0 : request.getSEcho() + 1)
                .dataList(orderReportLists)
                .iTotalRecords(count)
                .iTotalDisplayRecords(count)
                .build();
        return data;
    }

    @Override
    public void queryReportListExcel(OrderReportRequest request, OutputStream outputStream) throws Exception {
        // 获取  统计列表
        int count = reportMapper.countReport(request);
        if (count == Constants.EMPTY_CAPACITY) {
            throw new ExcelException(Status.WebErr.EMPTY_EXCEL.getCode(), Status.WebErr.EMPTY_EXCEL.getMsg());
        }
        if (count > Constants.EXCEL_SIZE) {
            throw new ExcelException(Status.WebErr.EXCEL_DATA_TOOBIG.getCode(), Status.WebErr.EXCEL_DATA_TOOBIG.getMsg());
        }

        // 获取 ： 应收金额、预约订单、停车订单
        OrderReportTotal orderReportTotal = new OrderReportTotal();
        this.getReportTitleInfo(request, orderReportTotal);
        List<OrderReportTotal> orderReportTotals = new ArrayList<>();
        orderReportTotals.add(orderReportTotal);
        // 第一个sheet
        List<Map<String, Object>> list1 = ExcelSheetUtil.dataToMap(orderReportTotals, OrderReportTotal.class);

        request.setStart(0);
        request.setLength(Constants.EXCEL_SIZE);
        List<OrderReportList> orderReportLists = reportMapper.queryReportList(request);
        // 第二个sheet
        List<Map<String, Object>> list2 = ExcelSheetUtil.dataToMap(orderReportLists, OrderReportList.class);

        List<List<Map<String, Object>>> mapLists = new ArrayList<>();
        mapLists.add(list1);
        mapLists.add(list2);

        ExcelSheetUtil.write(OrderReportList.class.getSimpleName(), mapLists, outputStream);
    }
}

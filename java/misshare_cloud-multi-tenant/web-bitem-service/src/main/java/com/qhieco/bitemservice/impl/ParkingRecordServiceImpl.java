package com.qhieco.bitemservice.impl;

import com.qhieco.bitemservice.ParkingRecordService;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.webbitem.ParkingRecordRequest;
import com.qhieco.response.data.web.AbstractPaged;
import com.qhieco.response.data.webbitem.ParkingRecordData;
import com.qhieco.util.ExcelUtil;
import com.qhieco.webbitemmapper.ParkingRecordMapper;
import com.qhieco.webservice.exception.ExcelException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/7/3 15:27
 * <p>
 * 类说明：
 * ${说明}
 */
@Service
@Slf4j
public class ParkingRecordServiceImpl implements ParkingRecordService {

    @Autowired
    private ParkingRecordMapper parkingRecordMapper;

    @Override
    public AbstractPaged<ParkingRecordData> queryParkingRecordList(ParkingRecordRequest request) {
        HashMap<String, Object> params = new HashMap<>(16);
        if (request.getStart() != null && request.getLength() != null) {
            params.put("startPage", request.getStart());
            params.put("pageSize", request.getLength());
        }
        params.put("queryType", request.getQueryType());
        params.put("startTime", request.getStartTime());
        params.put("endTime", request.getEndTime());
        params.put("parklotId", request.getParklotId());

        int count = parkingRecordMapper.countParkingRecordListByCondition(params);
        List<ParkingRecordData> parkingRecordDataList = null;
        if (count > 0) {
            parkingRecordDataList = parkingRecordMapper.queryParkingRecordListByCondition(params);
        }

        AbstractPaged<ParkingRecordData> data = AbstractPaged.<ParkingRecordData>builder()
                .sEcho(request.getSEcho() == null ? 1 : request.getSEcho() + 1)
                .dataList(parkingRecordDataList)
                .iTotalRecords(count)
                .iTotalDisplayRecords(count)
                .build();
        return data;
    }

    @Override
    public void queryParkingRecordListExcel(ParkingRecordRequest request, OutputStream outputStream) throws Exception {
        HashMap<String, Object> params = new HashMap<>(16);
        params.put("startPage", 0);
        params.put("pageSize", Constants.EXCEL_SIZE);
        params.put("queryType", request.getQueryType());
        params.put("startTime", request.getStartTime());
        params.put("endTime", request.getEndTime());
        params.put("parklotId", request.getParklotId());

        int count = parkingRecordMapper.countParkingRecordListByCondition(params);
        if (count == Constants.EMPTY_CAPACITY) {
            throw new ExcelException(Status.WebErr.EMPTY_EXCEL.getCode(), Status.WebErr.EMPTY_EXCEL.getMsg());
        }
        if (count > Constants.EXCEL_SIZE) {
            throw new ExcelException(Status.WebErr.EXCEL_DATA_TOOBIG.getCode(), Status.WebErr.EXCEL_DATA_TOOBIG.getMsg());
        }

        List<ParkingRecordData> parkingRecordDataList = parkingRecordMapper.queryParkingRecordListByCondition(params);
        if (parkingRecordDataList == null || parkingRecordDataList.size() == Constants.EMPTY_CAPACITY) {
            throw new ExcelException(Status.WebErr.EMPTY_EXCEL.getCode(), Status.WebErr.EMPTY_EXCEL.getMsg());
        }
        ExcelUtil<ParkingRecordData> excelUtil = new ExcelUtil<>(outputStream, ParkingRecordData.class);
        excelUtil.write(parkingRecordDataList);
    }
}

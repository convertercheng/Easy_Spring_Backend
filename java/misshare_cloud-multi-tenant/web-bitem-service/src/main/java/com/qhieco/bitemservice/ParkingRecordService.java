package com.qhieco.bitemservice;

import com.qhieco.request.webbitem.ParkingRecordRequest;
import com.qhieco.response.data.web.AbstractPaged;
import com.qhieco.response.data.webbitem.ParkingRecordData;

import java.io.OutputStream;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/7/3 15:26
 * <p>
 * 类说明：
 * 车辆进出记录service
 */
public interface ParkingRecordService {


    public AbstractPaged<ParkingRecordData> queryParkingRecordList(ParkingRecordRequest request);

    public void queryParkingRecordListExcel(ParkingRecordRequest request, OutputStream outputStream)  throws Exception;

}

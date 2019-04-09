package com.qhieco.response.data.webbitem;

import lombok.Data;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/7/3 17:54
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class ParkingRecordData {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private Integer id;
    private String plateNo;
    private Long reserveStartTime;
    private Long reserveEndTime;
    private Long realStartTime;
    private Long realEndTime;
    private Long parkingTime;
    private BigDecimal parkingFee;

    /**
     * 预约时段 如:自2018-6-11 14:30至2018-6-11 15:30
     */
    private String reserveTimeStr;
    /**
     * 停车时长 如：24:00:00
     */
    private String parkingTimeStr;

    public String getReserveTimeStr() {
        String str = "";
        if (reserveStartTime != null && reserveEndTime != null) {
            str = "自" + sdf.format(new Date(this.reserveStartTime))
                    + "至" + sdf.format(new Date(this.reserveEndTime));
        }
        return str;
    }

    public String getParkingTimeStr() {
        String str = "";
        if (parkingTime != null) {
            Long second = parkingTime / 1000L;
            Long resSecond = second % 60L;
            Long minute = second / 60L;
            Long resMinute = minute % 60L;
            Long hour = minute / 60L;
            str = String.format("%d:%d:%d", hour, resMinute, resSecond);
        } else {
            str = "-";
        }
        return str;
    }
}

package com.qhieco.barrier.boostedgoal.request;

import lombok.Data;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018-06-08 9:42
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class BoostedGoalParkingRequest {
    private String accessCode;
    private String businessCode;
    private String signType;
    private String sign;
    private String parkingID;
    private String plateNumber;
    private String beginDate;
    private String endDate;

    public BoostedGoalParkingRequest() {

    }

    public BoostedGoalParkingRequest(String accessCode, String businessCode, String signType, String sign, String plateNumber, String parkingID, String beginDate,
                                     String endDate) {
        this.accessCode=accessCode;
        this.businessCode=businessCode;
        this.signType=signType;
        this.sign=sign;
        this.parkingID=parkingID;
        this.plateNumber=plateNumber;
        this.beginDate=beginDate;
        this.endDate=endDate;
    }
}

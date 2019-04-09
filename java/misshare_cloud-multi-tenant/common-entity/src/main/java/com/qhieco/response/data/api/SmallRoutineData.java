package com.qhieco.response.data.api;

import lombok.Data;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018-06-25 16:17
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class SmallRoutineData{
    private Integer userId;
    private String openId;
    private String unionId;
    private Integer parkLotId;
    private Integer parklocId;
    private Integer parklocState;
    private String userPhone;
    private OrderParkingLockData orderParkingLockData;
    private Integer parklotDistrictId;
}

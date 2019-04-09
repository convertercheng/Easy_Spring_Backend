package com.qhieco.response.data.api;

import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/27 19:35
 * <p>
 * 类说明：
 * 停车场详情中车位可预约时段RESPONSE
 */
@lombok.Data
public class ParklocShareRepData {

    private Integer parklocId;

    private String parklocNumber;

//    private List<Share> shares;

    private List<ReserveTimeData> shares;

}

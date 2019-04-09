package com.qhieco.request.webbitem;

import com.qhieco.request.web.QueryPaged;
import lombok.Data;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/7/3 18:07
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class ParkingRecordRequest extends QueryPaged {

    /**
     * 1:进场时间
     * 2:出场时间
     */
    private Integer queryType;
    private Integer parklotId;
    private Long startTime;
    private Long endTime;
}

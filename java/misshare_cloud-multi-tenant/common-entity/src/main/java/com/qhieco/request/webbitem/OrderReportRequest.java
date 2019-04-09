package com.qhieco.request.webbitem;

import com.qhieco.request.web.QueryPaged;
import lombok.Data;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/7/5 14:37
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class OrderReportRequest extends QueryPaged {

    private Integer parklotId;

    private Long startTime;

    private Long endTime;
}

package com.qhieco.request.api;

import lombok.Data;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018-06-22 17:24
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class OrderParkingRequest extends AbstractRequest{
    private Integer reserveId;
    private Integer parkLotId;
    private Integer parkLoctId;
    private Integer tag;
}

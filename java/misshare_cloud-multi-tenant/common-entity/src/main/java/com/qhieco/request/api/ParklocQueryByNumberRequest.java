package com.qhieco.request.api;

import lombok.Data;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/20 13:46
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class ParklocQueryByNumberRequest extends  AbstractRequest {
    private Integer user_id;
    private String parkloc_number;
}

package com.qhieco.request.web;

import lombok.Data;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-22 下午3:03
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class RouterRequest extends QueryPaged {
    private String parklotName;
    private String name;
    private String manufacturerName;
    private String model;
    private String number;
    private Integer id;
}

package com.qhieco.request.web;

import lombok.Data;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-20 上午11:09
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class RelaymeterRequest extends QueryPaged {
    private String number;
    private String parklotName;
    private String name;
    private String manufacturerName;
    private String model;
}

package com.qhieco.request.web;

import lombok.Data;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/14 11:29
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class ActivityQuery extends QueryPaged {
    private String name;
    private Integer type;
    private Integer state;
}

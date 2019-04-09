package com.qhieco.request.web;

import lombok.Data;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/8 17:07
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class ParamsRequest extends QueryPaged{
    private Integer id;
    private String qhkey;
    private String qhvalue;
    private Byte level;
    private Integer state;
}

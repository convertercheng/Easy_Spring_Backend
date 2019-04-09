package com.qhieco.request.web;

import lombok.Data;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/3 10:57
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class BusinessRequest extends  QueryPaged{
    private String businessName;

    private Integer businessStatus;

    private Integer state;
}

package com.qhieco.request.web;

import lombok.Data;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/8 15:52
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class LogOperationWebRequest extends QueryPaged{
    private String content;

    private String sourceIp;

    private Long startOperateTime;

    private Long endOperateTime;
}

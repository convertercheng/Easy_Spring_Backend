package com.qhieco.request.web;

import lombok.Data;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-4-3 下午7:27
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class LogWebRequest {
    private String sourceIp;
    private String content;
    private Long startOperateTime;
    private Long endOperateTime;
}

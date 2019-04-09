package com.qhieco.request.api;

import lombok.Data;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/17 17:21
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class LogRequest extends AbstractRequest {

    private Integer user_id;
    /**
     * 操作内容
     */
    private String content;
    /**
     * 操作手机ip
     */
    private String source_ip;
    /**
     * 操作手机机型
     */
    private String source_model;
    /**
     * 类型
     */
    private Integer type;
}

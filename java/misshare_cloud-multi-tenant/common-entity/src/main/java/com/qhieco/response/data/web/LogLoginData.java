package com.qhieco.response.data.web;

import lombok.Data;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/7 15:27
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class LogLoginData {
    private Integer logId;
    private String phone;
    private String ip;
    private Long loginTime;
    private String phoneModel;
    //private Integer type;
}

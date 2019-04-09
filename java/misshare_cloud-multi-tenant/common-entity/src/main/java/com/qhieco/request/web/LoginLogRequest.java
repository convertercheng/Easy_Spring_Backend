package com.qhieco.request.web;

import lombok.Data;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/7 15:29
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class LoginLogRequest extends QueryPaged {
    private String phone;
    /**
     * 手机型号
     */
    private String phoneModel;
    private Long loginStartTime;
    private Long loginEndTime;
}

package com.qhieco.request.web;

import lombok.Data;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/7 14:49
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class UserListRequest extends QueryPaged {
    private Integer userId;
    private String phone;
    private Integer userType;
    /**
     * 状态，正常，异常
     */
    private Integer state;
    /**
     * 最后登录的时间范围，开始时间
     */
    private Long lastLoginStartTime;
    /**
     * 最后登录的时间范围，结束时间
     */
    private Long lastLoginEndTime;
    /**
     * 注册的时间范围，开始时间
     */
    private Long registerStartTime;
    /**
     * 注册的时间范围，结束时间
     */
    private Long registerEndTime;
}

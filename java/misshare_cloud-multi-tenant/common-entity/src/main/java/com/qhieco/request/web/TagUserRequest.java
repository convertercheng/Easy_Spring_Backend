package com.qhieco.request.web;

import lombok.Data;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/6/15 17:06
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class TagUserRequest extends QueryPaged {

    private Integer tagId;
    private String phone;
    /**
     * 用户类型，全部传-1
     */
    private Integer userType;
}

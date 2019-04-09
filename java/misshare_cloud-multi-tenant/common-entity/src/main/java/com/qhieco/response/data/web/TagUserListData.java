package com.qhieco.response.data.web;

import lombok.Data;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/6/15 17:01
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class TagUserListData {

    private Integer userId;
    private String phone;
    private String userTypeStr;
    /**
     * 用户是否绑定标签，0没绑定 1 绑定
     */
    private Integer isBind;

}

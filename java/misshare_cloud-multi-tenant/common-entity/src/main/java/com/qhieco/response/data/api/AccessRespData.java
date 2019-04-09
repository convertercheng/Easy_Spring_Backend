package com.qhieco.response.data.api;

import lombok.Data;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/13 11:21
 * <p>
 * 类说明：
 * 门禁响应实体
 */
@Data
public class AccessRespData {

    private Integer id;
    /**
     * 门禁名称
     */
    private String name;
    /**
     * 门禁蓝牙名称
     */
    private String btName;
    /**
     * 门禁蓝牙密码
     */
    private String btPwd;
}

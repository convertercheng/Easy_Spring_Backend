package com.qhieco.response.data.api;

import lombok.Data;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/14 14:40
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class FileRespData {
    private Integer id;
    /**
     * 文件名称
     */
    private String name;
    /**
     * 文件路径
     */
    private String path;
    /**
     * 文件说明
     */
    private String intro;
}

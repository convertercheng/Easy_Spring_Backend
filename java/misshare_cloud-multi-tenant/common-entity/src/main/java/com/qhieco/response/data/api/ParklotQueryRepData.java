package com.qhieco.response.data.api;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/2/24 14:20
 * <p>
 * 类说明：
 * ${说明}
 */
@lombok.Data
public class ParklotQueryRepData {
    /**
     * 小区名
     */
    private String name;
    /**
     * 小区id
     */
    private Integer id;
    /**
     * 小区地址
     */
    private String address;
    /**
     * 停车区类型，0是静态车场车位查询，1是约车场，2是约车位
     */
    private Integer type;
}

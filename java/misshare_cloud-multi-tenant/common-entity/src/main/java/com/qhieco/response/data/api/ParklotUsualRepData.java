package com.qhieco.response.data.api;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/2/24 13:45
 * <p>
 * 类说明：
 * 查询常用停车场返回实体类
 */
@lombok.Data
public class ParklotUsualRepData {
    /**
     * 停车场id
     */
    private Integer parklotId;
    /**
     * 常用车场表id
     */
    private Integer usualId;
    /**
     * 车场名称
     */
    private String name;
    /**
     * 停车区类型，0是静态车场车位查询，1是约车场，2是约车位
     */
    private Integer type;
}

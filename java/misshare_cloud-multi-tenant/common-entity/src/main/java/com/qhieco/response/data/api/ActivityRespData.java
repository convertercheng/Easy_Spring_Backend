package com.qhieco.response.data.api;

import lombok.Data;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/4 12:34
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class ActivityRespData {
    private Integer id;
    private String name;
    private Integer type;
    private String filePath;
    private String intro;
    private String href;
    private Long beginTime;
    private Long endTime;
    private Integer triggerType;
}

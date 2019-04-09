package com.qhieco.response.data.api;

import lombok.Data;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/13 16:38
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class PublishParklocRespData {
    /**
     * 车位id
     */
    private Integer parklocId;
    /**
     * 车场id
     */
    private Integer parklotId;
    /**
     * 车场名称
     */
    private String parklotName;
    /**
     * 车位编号
     */
    private String parklocNumber;

    private Integer state;

    /**
     * 车位发布信息列表
     */
    private List<PublishRespData> publishList;
}

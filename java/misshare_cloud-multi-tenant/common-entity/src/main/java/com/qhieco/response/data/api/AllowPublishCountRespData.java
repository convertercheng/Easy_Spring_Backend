package com.qhieco.response.data.api;

import lombok.Data;
import lombok.Setter;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/15 9:12
 * <p>
 * 类说明：
 *     允许发布的车位数量
 */
@Data
public class AllowPublishCountRespData {
    private Integer count;

    private Integer parklotId;

    private String parklocIds;
}

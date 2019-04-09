package com.qhieco.request.api;

import lombok.Data;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 14:16
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class ReserveEnterRequest extends AbstractRequest {

    private Integer user_id;
    /**
     * 车场id
     */
    private Integer parklot_id;
    /**
     * 车位Id
     */
    private Integer parkloc_id;
    /**
     * 区域id
     */
    private Integer district_id;
}

package com.qhieco.request.api;

import lombok.Data;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/13 17:18
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class PublishParklocQueryRequest extends AbstractRequest {
    private Integer user_id;
    /**
     * 车位状态， 1101表示已发布（共享时间），车位列表则不需要传参数，或者传-1
     */
    private Integer state;

    private Integer page_num;
}

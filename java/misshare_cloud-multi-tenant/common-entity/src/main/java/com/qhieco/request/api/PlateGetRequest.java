package com.qhieco.request.api;

import lombok.Data;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/2/27 11:08
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class PlateGetRequest extends AbstractRequest {
    private Integer user_id;
    private Integer page_num;
}

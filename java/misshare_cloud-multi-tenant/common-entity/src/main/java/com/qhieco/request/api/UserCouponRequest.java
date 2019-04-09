package com.qhieco.request.api;

import lombok.Data;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 13:55
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class UserCouponRequest extends AbstractRequest {

    private Integer user_id;
    /**
     * 当前页码，从0开始
     */
    private Integer page_num;
}

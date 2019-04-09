package com.qhieco.request.api;

import lombok.Data;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 15:56
 * <p>
 * 类说明：
 * 开具电子发票请求参数
 */
@Data
public class CouponIdRequest extends AbstractRequest{
    private Integer coupon_id;
}

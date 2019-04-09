package com.qhieco.request.api;

import lombok.Data;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/23 16:48
 * <p>
 * 类说明：
 * 广告页方法请求参数
 */
@Data
public class UpdateRequest extends AbstractRequest {
    /**
     * 适配的手机类型 0：安卓 1：iOS
     */
    private Integer type;

}

package com.qhieco.request.api;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/2/24 下午4:39
 * <p>
 * 类说明：
 *     获取验证码的请求类
 */
public class VerificationCodeGetRequest extends AbstractRequest{


    /**
     * phone : xxxx...xxxx
     * anti: xxx
     * timestamp : xxxx...xxxx
     */
    @Setter
    @Getter
    private String phone;

    @Setter
    @Getter
    private Integer anti;
}

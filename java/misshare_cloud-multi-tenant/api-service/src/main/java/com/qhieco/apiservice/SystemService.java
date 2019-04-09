package com.qhieco.apiservice;

import com.qhieco.request.api.VerificationCodeGetRequest;
import com.qhieco.response.data.api.VerificationCodeGetRepData;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/2/24 下午4:54
 * <p>
 * 类说明：
 *     SystemService
 */

public interface SystemService {
    /**
     * 该方法返回用户获取验证码的结果
     * @param verificationCodeGetRequest 获取验证码的请求参数
     * @return 返回请求结果
     */
    VerificationCodeGetRepData getVerificationCode(VerificationCodeGetRequest verificationCodeGetRequest);
}

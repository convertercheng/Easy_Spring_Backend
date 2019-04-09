package com.qhieco.barrier.epaitc.request;

import lombok.Data;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/4/2 下午3:55
 * <p>
 * 类说明：
 *     Epatic的登录请求接口
 */
@Data
public class LoginRequest {


    /**
     * username : xxxxx
     * password : xxxxxxxxx
     */

    private String username;
    private String password;

}

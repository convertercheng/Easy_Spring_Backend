package com.qhieco.request.api;

import lombok.Data;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/2/24 上午9:01
 * <p>
 * 类说明：
 *     抽象Request类
 */
@Data
public abstract class AbstractRequest {

    /**
     * token : xxx
     * timestamp : xxx
     * signature : xxx
     */

    private String token;
    private String timestamp;
    private String signature;
}

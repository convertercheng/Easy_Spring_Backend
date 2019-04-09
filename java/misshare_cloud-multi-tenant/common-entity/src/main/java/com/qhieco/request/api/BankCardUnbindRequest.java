package com.qhieco.request.api;

import lombok.Data;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/7 上午12:57
 * <p>
 * 类说明：
 *     解绑银行卡请求类
 */
@Data
public class BankCardUnbindRequest extends AbstractRequest{


    /**
     * user_id : 1
     * bankcard_id : 2
     */

    private Integer user_id;
    private Integer bankcard_id;
}

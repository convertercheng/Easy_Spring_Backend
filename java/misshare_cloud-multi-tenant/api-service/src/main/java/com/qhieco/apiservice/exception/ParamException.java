package com.qhieco.apiservice.exception;

import com.qhieco.constant.Status;
import com.qhieco.response.Resp;
import lombok.Getter;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/2/25 下午11:37
 * <p>
 * 类说明：
 *     参数的Exception类
 */

public class ParamException extends RuntimeException {

    @Getter
    private Integer code;

    public ParamException(Resp resp, Integer code) {
        super(resp.getError_message());
        this.code = code;
    }

}

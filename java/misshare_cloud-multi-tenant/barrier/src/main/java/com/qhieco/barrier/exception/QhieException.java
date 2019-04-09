package com.qhieco.barrier.exception;

import com.qhieco.constant.Status;
import lombok.Getter;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/2/25 下午11:37
 * <p>
 * 类说明：
 *     自定义的Exception基类
 */

public class QhieException extends RuntimeException {

    @Getter
    private Integer code;

    public QhieException(Status.ApiErr apiErr) {
        super(apiErr.getMsg());
        this.code = apiErr.getCode();
    }

}

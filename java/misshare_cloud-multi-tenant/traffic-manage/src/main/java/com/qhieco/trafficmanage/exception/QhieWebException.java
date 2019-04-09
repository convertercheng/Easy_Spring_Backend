package com.qhieco.trafficmanage.exception;

import com.qhieco.constant.Status;
import lombok.Getter;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/2/25 下午11:37
 * <p>
 * 类说明：
 *     自定义的Exception基类，供Web使用
 */

public class QhieWebException extends RuntimeException {

    @Getter
    private Integer code;

    public QhieWebException(Status.WebErr webErr) {
        super(webErr.getMsg());
        this.code = webErr.getCode();
    }

}

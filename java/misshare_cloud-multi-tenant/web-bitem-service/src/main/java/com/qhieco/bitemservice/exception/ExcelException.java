package com.qhieco.bitemservice.exception;

import com.qhieco.response.Resp;
import lombok.Getter;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-7 下午3:55
 * <p>
 * 类说明：
 * ${description}
 */
public class ExcelException extends RuntimeException{
    @Getter
    private Integer code;

    public ExcelException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }
}

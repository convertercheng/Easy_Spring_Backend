package com.example.basic_backend.backend.Bussniss_logic.Entity;

/**
 * @程志豪 时间：15:17
 * 描述：{定义标准的response的主体，便于修改内容和异常处理}
 **/
public class StandardResp<T> {
    private Integer Errorcode;
    private String ErrorMessage;
    private  T data;
}

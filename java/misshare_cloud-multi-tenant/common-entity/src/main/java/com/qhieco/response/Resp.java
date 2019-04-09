package com.qhieco.response;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/2/23 17:40
 * <p>
 * 类说明：
 *     Resp返回
 */
@lombok.Data
public class Resp<T> {

    /**
     * error_code : 2000
     * error_message : 错误信息
     * data : {}
     */

    private Integer error_code;
    private String error_message;
    private T data;
}

package com.qhieco.util;

import com.qhieco.constant.Status;
import com.qhieco.response.Resp;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/2/23 17:46
 * <p>
 * 类说明：
 *     返回工具类
 */
public class RespUtil {

    public static <T> Resp<T> successResp(T data) {
        Resp<T> resp = new Resp<>();
        resp.setError_code(Status.ApiErr.SUCCESS.getCode());
        resp.setError_message(Status.ApiErr.SUCCESS.getMsg());
        resp.setData(data);
        return resp;
    }

    public static Resp successResp() {
        return successResp(null);
    }

    public static Resp errorResp(Integer errCode, String errMsg) {
        Resp resp = new Resp();
        resp.setError_code(errCode);
        resp.setError_message(errMsg);
        resp.setData(null);
        return resp;
    }

}

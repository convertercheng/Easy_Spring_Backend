package com.qhieco.juhe;

import java.util.List;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/2/26 下午1:47
 * <p>
 * 类说明：
 *     聚合数据短信response
 */

public class JuheSmsResponse {


    /**
     * reason : 错误的短信模板ID,请通过后台确认!!!
     * result : []
     * error_code : 205402
     */

    private String reason;
    private int error_code;
    private List<?> result;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public List<?> getResult() {
        return result;
    }

    public void setResult(List<?> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "JuheSmsResponse{" +
                "reason='" + reason + '\'' +
                ", error_code=" + error_code +
                ", result=" + result +
                '}';
    }
}

package com.qhieco.response.data.api;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/27 10:20
 * <p>
 * 类说明：
 *   银行4元素验证响应参数
 */
@lombok.Data
public class VerifyBankCard4RepData {
    private Integer error_code;
    private String reason;
    private Result result;

    @lombok.Data
    public class Result{
        private String jobid;
        private String realname;
        private String bankcard;
        private String idcard;
        private String mobile;
        private String res;
        private String message;
    }

}

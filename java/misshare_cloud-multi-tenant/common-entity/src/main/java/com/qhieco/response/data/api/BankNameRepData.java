package com.qhieco.response.data.api;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/27 10:20
 * <p>
 * 类说明：
 *   银行信息响应参数
 */
@lombok.Data
public class BankNameRepData {
    private Integer error_code;
    private String reason;
    private Result result;

    @lombok.Data
    class Result{
        private String bank;
        private String type;
        private String tel;
        private String logo;
    }

}

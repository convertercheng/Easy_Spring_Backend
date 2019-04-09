package com.qhieco.request.api;

import lombok.Data;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/6 10:39
 * <p>
 * 类说明：
 *       添加银行卡请求类
 */
@Data
public class BankCardAddRequest extends AbstractRequest {



    private Integer user_id;

    private String bank_number;

    private String reserved_phone;

    private String bank;

    private String type;



}

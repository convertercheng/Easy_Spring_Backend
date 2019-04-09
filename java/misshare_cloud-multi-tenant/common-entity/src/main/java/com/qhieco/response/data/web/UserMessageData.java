package com.qhieco.response.data.web;

import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/4/7 9:55
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class UserMessageData {

    private Integer userId;

    private String jpushId;

    private String phone;


}

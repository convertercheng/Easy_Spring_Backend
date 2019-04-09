package com.qhieco.request.web;

import com.qhieco.request.api.AbstractRequest;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 15:56
 * <p>
 * 类说明：
 * 添加/修改标签请求参数
 */
@Data
public class TagRequest extends AbstractRequest {

    Integer id;

    String name;

    String comment;

    Integer userType;

    Long startSignupTime;

    Long endSignupTime;

    Integer orderNumber;

    BigDecimal orderAmount;

    Integer unsigninDays;



}

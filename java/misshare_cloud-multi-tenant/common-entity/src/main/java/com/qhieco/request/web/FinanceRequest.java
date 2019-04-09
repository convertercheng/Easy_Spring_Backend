package com.qhieco.request.web;

import lombok.Data;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/27 下午3:45
 * <p>
 * 类说明：
 *     添加预约费用请求类
 */
@Data
public class FinanceRequest {

    private Long startTime;

    private Long endTime;

    private Integer type;

}

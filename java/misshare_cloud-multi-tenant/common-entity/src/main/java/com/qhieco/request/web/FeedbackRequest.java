package com.qhieco.request.web;

import lombok.Data;

import java.util.List;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-4-3 上午11:52
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class FeedbackRequest extends QueryPaged{
    private String phone;
    private Long startCreateTime;
    private Long endCreateTime;
    private List<Integer> problems;
}

package com.qhieco.request.web;

import lombok.Data;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/5/17 15:19
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class FeekbackRequest extends QueryPaged{
    private String phone;
    private Long startTime;
    private Long endTime;
    private List<Integer> proIdList;
}

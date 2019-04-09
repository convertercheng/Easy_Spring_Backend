package com.qhieco.request.web;

import lombok.Data;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/6/13 9:25
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class StatisticsRequest extends QueryPaged{

    private Integer id;
    private Integer activityId;//活动ID
    private Integer activityType; //活动类型，1：注册类型，2：首次下单，3：绑定车牌
    private Integer type;//活动统计类型,1:浏览量，2:参与人数，3:获奖人数，4:活动触发
    private Integer userId;//用户ID
    private Integer value;//活动统计value值,正常情况值为 1
    private Long createTime;//创建时间

    private Long beginStatistTime;//检索起始时间
    private Long endStatistTime;//检索结束时间
    private String dateType;//检索活动统计时间类型

}

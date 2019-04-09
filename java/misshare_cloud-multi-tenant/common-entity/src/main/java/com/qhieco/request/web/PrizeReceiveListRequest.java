package com.qhieco.request.web;

import lombok.Data;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/6/13 11:30
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class PrizeReceiveListRequest extends QueryPaged {
    /**
     * 奖品名称
     */
    private String prizeName;
    /**
     * 领取用户
     */
    private String phone;
    /**
     * 奖励触发途径
     */
    private Integer triggerType;

    private Long time;
}

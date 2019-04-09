package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/6/13 14:06
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
@Entity
@Table(name = "t_prize_receive_record")
public class PrizeReceiveRecord {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "prize_id")
    private Integer prizeId;

    @Column(name = "mobile_user_id")
    private Integer mobileUserId;

    @Column(name = "activity_id")
    private Integer activityId;

    @Column(name = "activity_rule_id")
    private Integer activityRuleId;

    @Column(name = "phone")
    private String phone;

    @Column(name = "number")
    private Integer number;

    @Column(name = "create_time")
    private Long createTime;
}

package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/6/12 10:50
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
@Entity
@Table(name = "t_activity_rule_trigger")
public class ActivityRuleTrigger {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "activity_rule_id")
    private Integer activityRuleId;

    @Column(name = "trigger_type")
    private Integer triggerType;

    @Column(name = "create_time")
    private Long createTime;

    @Column(name = "state")
    private Integer state;

    public ActivityRuleTrigger(){}

    public ActivityRuleTrigger(Integer activityRuleId,Integer triggerType,Long createTime,Integer state){
        this.activityRuleId = activityRuleId;
        this.triggerType = triggerType;
        this.createTime = createTime;
        this.state = state;
    }

}

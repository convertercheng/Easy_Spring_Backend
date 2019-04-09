package com.qhieco.commonentity;

import com.qhieco.commonentity.relational.LadderPrizeB;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/6/12 10:34
 * <p>
 * 类说明：
 * 阶梯表
 */
@Data
@Entity
@Table(name = "t_ladder")
public class Ladder {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "activity_rule_id")
    private Integer activityRuleId;

    @Column(name = "ladder_step")
    private Integer ladderStep;

    @Column(name = "type")
    private Integer type;

    @Column(name = "invite_number")
    private Integer inviteNumber;

    @Column(name = "create_time")
    private Long createTime;

    @Column(name = "state")
    private Integer state;

    @Transient
    private List<LadderPrizeB> prizes;



}

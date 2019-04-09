package com.qhieco.commonentity;

import com.qhieco.response.data.web.Trigger;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/6/12 11:14
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
@Entity
@Table(name = "t_activity_rule")
public class ActivityRule {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "activity_id")
    private Integer activityId;

    @Column(name = "type")
    private Integer type;

    @Column(name = "create_time")
    private Long createTime;

    @Column(name = "state")
    private Integer state;

    @Transient
    private List<Trigger> triggers;

    @Transient
    private List<Ladder> ladders;


    public ActivityRule(){}


    public ActivityRule(Integer activityId,Integer type,Long createTime,Integer state){
        this.activityId = activityId;
        this.type = type;
        this.createTime = createTime;
        this.state = state;
    }
}

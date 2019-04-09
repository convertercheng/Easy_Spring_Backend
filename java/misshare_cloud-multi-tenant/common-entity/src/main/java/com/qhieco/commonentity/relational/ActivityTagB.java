package com.qhieco.commonentity.relational;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/6/12 11:18
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
@Entity
@Table(name = "b_activity_tag")
public class ActivityTagB {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "activity_id")
    private Integer activityId;

    @Column(name = "tag_id")
    private Integer tagId;

    @Column(name = "type")
    private Integer type;

    @Column(name = "create_time")
    private Long createTime;

    @Column(name = "state")
    private Integer state;
}

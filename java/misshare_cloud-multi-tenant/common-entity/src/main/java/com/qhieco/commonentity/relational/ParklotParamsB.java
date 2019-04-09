package com.qhieco.commonentity.relational;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/8 17:46
 * <p>
 * 类说明：
 * 停车区参数关联表
 */
@Entity
@Table(name = "b_parklot_params")
@Data
public class ParklotParamsB {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "parklot_id")
    private Integer parklotId;

    @Column(name = "qhkey")
    private String qhKey;

    @Column(name = "qhvalue")
    private String qhValue;

    @Column(name = "update_time")
    private Long updateTime;

    @Column(name = "state")
    private Integer state;

    @Column(name = "name")
    private String name;

    @Column(name = "sequence")
    private Integer sequence;

    @Column(name = "group_name", nullable = false)
    private String groupName;

    public ParklotParamsB() {
    }

    public ParklotParamsB(Integer parklotId, Long updateTime, Integer state, String name, String groupName) {
        this.parklotId = parklotId;
        this.updateTime = updateTime;
        this.state = state;
        this.name = name;
        this.groupName = groupName;
    }
}
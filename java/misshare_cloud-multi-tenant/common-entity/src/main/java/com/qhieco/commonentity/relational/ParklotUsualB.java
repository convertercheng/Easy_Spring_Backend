package com.qhieco.commonentity.relational;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/2/8 下午11:17
 * <p>
 * 类说明：
 * 用户常用车场表
 */
@Data
@Entity
@Table(name = "b_parklot_usual")
public class ParklotUsualB {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;


    /**
     * 手机用户表关联id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 停车场表关联id
     */
    @Column(name = "parklot_id")
    private Integer parklotId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Long createTime;

    /**
     * 等级
     */
    @Column(name = "level")
    private Integer level;

    /**
     * 状态
     */
    @Column(name = "state")
    private Integer state;

    public ParklotUsualB(){};

    public ParklotUsualB(Integer userId,Integer parklotId,Long createTime,Integer level,Integer state){
        this.userId = userId;
        this.parklotId = parklotId;
        this.createTime = createTime;
        this.level = level;
        this.state = state;
    }
}

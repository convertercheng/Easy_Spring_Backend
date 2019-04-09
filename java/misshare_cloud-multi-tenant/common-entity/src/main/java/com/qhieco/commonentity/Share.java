package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应共享表
 */
@Data
@Entity
@Table(name = "t_share")
public class Share {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;

    /**
     * 停车位Id
     */
    @Column(name = "parkloc_id")
    private Integer parklocId;

    /**
     * 发布id
     */
    @Column(name = "publish_id")
    private Integer publishId;

    /**
     * 开始时间
     */
    @Column(name = "start_time")
    private Long startTime;

    /**
     * 结束时间
     */
    @Column(name = "end_time")
    private Long endTime;

    /**
     * 状态
     */
    @Column(name = "state")
    private Integer state;

    public Share(){}

    public Share(Integer parklocId,Integer publishId,Long startTime,Long endTime,Integer state){
        this.parklocId = parklocId;
        this.publishId = publishId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.state = state;
    }
}
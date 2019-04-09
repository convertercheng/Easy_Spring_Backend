package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应预约表
 */
@Data
@Entity
@Table(name = "t_reservation")
public class Reservation {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "mobile_user_id", nullable = false)
    private Integer mobileUserId;

    @Column(name = "share_id", nullable = false)
    private Integer shareId;

    @Column(name = "start_time", nullable = false)
    private Long startTime;

    @Column(name = "end_time", nullable = false)
    private Long endTime;

    public Reservation(){}

    public Reservation(Integer mobileUserId,Integer shareId,Long startTime,Long endTime){
        this.mobileUserId = mobileUserId;
        this.shareId = shareId;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
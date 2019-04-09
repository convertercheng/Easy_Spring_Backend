package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应文件表
 */
@Data
@Entity
@Table(name = "t_reserve_filter")
public class ReserveFilter {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;

    /**
     * 用户Id
     */
    @Column(name = "mobile_user_id")
    private Integer mobileUserId;

    /**
     * 车场Id
     */
    @Column(name = "parklot_id")
    private Integer parklotId;

    /**
     * 预约开始时间
     */
    @Column(name = "start_time")
    private Long startTime;

    /**
     * 预约结束时间
     */
    @Column(name = "end_time")
    private Long endTime;


}
package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应车位锁日志表
 */
@Entity
@Table(name = "t_log_lock")
@Data
public class LogLock {

    public LogLock(){}

    public LogLock(Integer lockId,Integer rockerState,Integer buzzerState,Integer geomagneticState,Integer workState,Double battery,Long createTime){
        this.lockId = lockId;
        this.rockerState = rockerState;
        this.buzzerState = buzzerState;
        this.geomagneticState = geomagneticState;
        this.workState = workState;
        this.battery = battery;
        this.createTime = createTime;
    }

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "lock_id")
    private Integer lockId;

    @Column(name = "rocker_state")
    private Integer rockerState;

    @Column(name = "buzzer_state")
    private Integer buzzerState;

    @Column(name = "geomagnetic_state")
    private Integer geomagneticState;

    @Column(name = "work_state")
    private Integer workState;

    @Column(name = "battery")
    private Double battery;

    @Column(name = "create_time")
    private Long createTime;

    @Transient
    private String  lockNumber;

    @Transient
    private String lockMac;

    @Transient
    private String identifier;


}
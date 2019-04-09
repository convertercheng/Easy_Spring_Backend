package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/2/5 下午11:00
 * <p>
 * 类说明：
 * Plate实体类，对应车牌号表
 */
@Entity
@Table(name = "t_plate")
@Data
public class Plate {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "number")
    private String number;

    @Column(name = "create_time")
    private Long createTime;

    @Column(name = "state")
    private Integer state;

    public Plate(){}

    public Plate(String number,Long createTime,Integer state){
        this.number = number;
        this.createTime = createTime;
        this.state = state;
    }
}

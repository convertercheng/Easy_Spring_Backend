package com.qhieco.commonentity.relational;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/6/12 11:22
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
@Entity
@Table(name = "b_ladder_prize")
public class LadderPrizeB {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "ladder_id")
    private Integer ladderId;

    @Column(name = "prize_id")
    private Integer prizeId;

    @Column(name = "prize_number")
    private Integer prizeNumber;

    @Column(name = "create_time")
    private Long createTime;

    @Column(name = "state")
    private Integer state;



}

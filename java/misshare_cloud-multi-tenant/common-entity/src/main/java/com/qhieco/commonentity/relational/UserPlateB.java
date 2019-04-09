package com.qhieco.commonentity.relational;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应用户车牌关联表
 */
@Entity
@Table(name = "b_user_plate")
@Data
public class UserPlateB {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "mobile_user_id")
    private Integer mobileUserId;

    @Column(name = "plate_id")
    private Integer plateId;

    @Column(name = "state")
    private Integer state;

    @Column(name = "update_time")
    private Long updateTime;


    public UserPlateB(){}

    public UserPlateB(Integer mobileUserId,Integer plateId,Integer state,Long updateTime){
        this.mobileUserId = mobileUserId;
        this.plateId = plateId;
        this.state = state;
        this.updateTime = updateTime;
    }
}
package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应申请车位类
 */
@Entity
@Table(name = "t_apply_parkloc")
@Data
public class ApplyParkloc {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "mobile_user_id")
    private Integer mobileUserId;

    @Column(name = "web_user_id")
    private Integer webUserId;

    @Column(name = "area_id")
    private Integer areaId;

    @Column(name = "parklot_name")
    private String parklotName;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "message")
    private String message;

    @Column(name = "apply_time")
    private Long applyTime;

    @Column(name = "complete_time")
    private Long completeTime;

    @Column(name = "state")
    private Integer state;

    public ApplyParkloc(){}

    public ApplyParkloc(Integer mobileUserId,Integer areaId,String parklotName,String contactPhone,Long applyTime,Integer state){
        this.mobileUserId = mobileUserId;
        this.areaId = areaId;
        this.parklotName = parklotName;
        this.contactPhone = contactPhone;
        this.applyTime = applyTime;
        this.state = state;
    }
}
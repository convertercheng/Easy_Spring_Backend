package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应反馈表
 */
@Data
@Entity
@Table(name = "t_feedback")
public class Feedback {

    public Feedback(){}

    public Feedback(Integer mobileUserId,Integer parklotId,String remark,Long createTime,String fileIds){
        this.mobileUserId = mobileUserId;
        this.parklotId = parklotId;
        this.remark = remark;
        this.createTime = createTime;
        this.fileIds = fileIds;
    }

    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private Integer mobileUserId;

    @Column
    private Integer parklotId;

    @Column
    private String remark;

    @Column
    private Long createTime;

    @Column
    private String fileIds;

    @Transient
    private String phone;

    @Transient
    private String parklotName;

    @Transient
    private String problem;
}
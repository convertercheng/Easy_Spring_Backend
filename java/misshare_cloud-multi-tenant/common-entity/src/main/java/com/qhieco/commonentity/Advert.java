package com.qhieco.commonentity;

import com.qhieco.constant.Status;
import lombok.Data;

import javax.persistence.*;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/2/23 17:51
 * <p>
 * 类说明：
 * 广告页实体类
 */
@Data
@Entity
@Table(name = "t_advert")
public class Advert {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;

    /**
     * 图片表关联id
     */
    @Column(name = "file_id")
    private Integer fileId;

    /**
     * 跳转链接
     */
    @Column(name = "href")
    private String href;

    /**
     * 倒计时
     */
    @Column(name = "countdown")
    private Integer countdown;

    /**
     * 0表示无效，1表示有效
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 是否可跳 0可跳，1不可跳
     */
    @Column(name = "jumpable")
    private Integer jumpable;

    @Column(name = "create_time")
    private Long createTime;

    /**
     * 适配的手机类型 0：安卓 1：iOS
     */
    @Column(name = "phone_type")
    private Integer phoneType;


    /**
     * desc的字段都是中文描述使用
     */
    @Transient
    private String stateDesc;
    @Transient
    private String jumpableDesc;
    @Transient
    private String phoneTypeDesc;

    public String getStateDesc() {
        return Status.Common.find(this.state);
    }

    public String getJumpableDesc() {
        return Status.Jumpable.find(this.jumpable);
    }

    public String getPhoneTypeDesc() {
        return Status.PhoneType.find(this.phoneType);
    }
}

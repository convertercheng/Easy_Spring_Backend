package com.qhieco.commonentity.relational;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应停车区管理员关联表
 */
@Entity
@Table(name = "b_parklot_admin")
@Data
public class ParklotAdminB {
    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private Integer mobileUserId;

    @Column
    private Integer parklotId;

    @Column
    private Integer state;

    @Column
    private Long createTime;

    @Column
    private Long modifyTime;
}
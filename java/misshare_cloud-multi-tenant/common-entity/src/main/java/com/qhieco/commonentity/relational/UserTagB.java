package com.qhieco.commonentity.relational;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应用户标签关联表
 */
@Data
@Entity
@Table(name = "b_user_tag")
public class UserTagB {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "mobile_user_id")
    private Integer mobileUserId;

    @Column(name = "tag_id")
    private Integer tagId;

    @Column(name = "create_time")
    private Long createTime;

    @Column(name = "modify_time")
    private Long modifyTime;

    @Column(name = "state")
    private Integer state;
}
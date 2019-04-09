package com.qhieco.commonentity.relational;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/13 16:20
 * <p>
 * 类说明：
 * 绑定用户来源标记实体类
 */
@Data
@Table(name = "b_user_register")
@Entity
public class UserRegisterB {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "identification")
    private String identification;

    @Column(name = "moblie_user_id")
    private Integer moblieUserId;

    @Column(name = "register_id")
    private Integer registerId;

    @Column(name = "update_time")
    private Long updateTime;

    @Column(name = "state")
    private Integer state;
}

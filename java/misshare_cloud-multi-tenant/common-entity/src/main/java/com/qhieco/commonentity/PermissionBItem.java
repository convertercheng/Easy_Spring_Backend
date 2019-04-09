package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/7/2 17:38
 * <p>
 * 类说明：
 * B端对应权限表
 */
@Data
@Entity
@Table(name = "bi_permission")
public class PermissionBItem {

    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private Integer pid;

    @Column(unique = true)
    private String name;

    @Column
    private String url;

    @Column
    private Long createTime;

    @Column
    private Integer state;

}
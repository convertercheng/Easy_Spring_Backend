package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 19-1-10 上午9:36
 * <p>
 * 类说明：
 * 停车场物业租户表　存储使用云服务的租户基础信息　id用于多个数据表的数据隔离标识
 */
@Data
@Entity
@Table(name = "t_tenant")
public class Tenant {

    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String name;

    @Column
    private Long createTime;

    @Column
    private String description;

    /**
     * 后续添加企业信息字段
     */
}

package com.qhieco.commonentity.relational;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-4-4 下午3:42
 * <p>
 * 类说明：
 * ${description}
 */
@Data
@Entity
@Table(name = "bi_role_permission")
public class RolePermissionBItem {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "permission_id")
    private Integer permissionId;

}

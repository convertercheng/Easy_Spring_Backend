package com.qhieco.commonentity;

import lombok.Data;
import javax.persistence.*;
import java.util.Set;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应角色表
 */
@Entity
@Table(name = "bi_role")
@Data
public class RoleBItem {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false,unique = true)
    private String name;

    @Column(nullable = false, name = "create_time")
    private Long createTime;

    @Column(nullable = false)
    private Integer state;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "bi_role_permission",
            joinColumns=@JoinColumn(name="role_id",foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT)),
            inverseJoinColumns=@JoinColumn(name="permission_id",foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT)))
    private Set<PermissionBItem> permissions;
}
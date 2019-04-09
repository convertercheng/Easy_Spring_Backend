package com.qhieco.commonentity;

import com.qhieco.TenantSupport;
import lombok.Data;
import org.hibernate.annotations.Filter;

import javax.persistence.ForeignKey;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应角色表
 */
@Entity
@Table(name = "t_role")
@Data
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class Role implements TenantSupport {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false,unique = true)
    private String name;

    @Column(nullable = false, name = "create_time")
    private Long createTime;

    @Column(nullable = false)
    private Integer state;

    @Column(nullable = false)
    private Integer level;

    @Column
    private Integer tenantId;

    @Transient
    private String tenantName;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "b_role_permission",
            joinColumns=@JoinColumn(name="role_id",foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT)),
            inverseJoinColumns=@JoinColumn(name="permission_id",foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT)))
    private Set<Permission> permissions;
}
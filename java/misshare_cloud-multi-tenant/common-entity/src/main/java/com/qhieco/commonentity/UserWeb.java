package com.qhieco.commonentity;

import com.qhieco.TenantSupport;
import com.qhieco.constant.Constants;
import lombok.Data;
import org.hibernate.annotations.Filter;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应web用户表
 */
@Entity
@Table(name = "t_user_web")
@Data
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class UserWeb implements Serializable, TenantSupport{

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String name;

    @Column(name = "create_time", nullable = false)
    private Long createTime;

    @Column(name = "latest_login_time", nullable = false)
    private Long latestLoginTime;

    @Column(nullable = false)
    private Integer state;

    /**
     * 账户等级：
     * １：平台管理员
     * ２：父账户
     * 3 :子账户
     */
    @Column(nullable = false)
    private Integer level;

    /**
     * 父账户ｉｄ，仅子账号具有,其他类型用户为 -1
     */
    @Column
    private Integer parentId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "b_user_role",
            joinColumns = @JoinColumn(name = "web_user_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT)),
            inverseJoinColumns = @JoinColumn(name = "role_id"), foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private Set<Role> roles;

    @Column
    private Integer tenantId;

    @Transient
    private String tenantName;

    @Transient
    private String roleName;

    public void genarateRoleName() {
        List<String> nameList = new ArrayList<>();
        if (this.roles == null) {
            return;
        }
        this.roles.forEach(role -> nameList.add(role.getName()));
        this.roleName = String.join(",", nameList);
        this.roles = null;
    }

    public Set<Permission> findPermission() {
        Set<Permission> permissionSet = new HashSet<>();
        if (this.roles != null) {
            this.roles.forEach(role -> permissionSet.addAll(role.getPermissions()));
        }
        return permissionSet;
    }

    public Boolean isSuper() {
        //TODO 询问宇神 超级管理员的判断方式是否与这里不同 改成通过level来判断
//        return this.level == 1;
        return Constants.SUPER_ADMIN.equals(this.username);
    }

    public Boolean isFather() {
        //TODO 更改成通过移位运算来判断是否是父账户
        return this.level == 2;

    }

}
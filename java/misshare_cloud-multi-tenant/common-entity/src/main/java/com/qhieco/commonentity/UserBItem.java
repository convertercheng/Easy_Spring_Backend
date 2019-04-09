package com.qhieco.commonentity;

import com.qhieco.constant.Constants;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/7/2 17:32
 * <p>
 * 类说明：
 * 对应B端web用户表
 */
@Entity
@Table(name = "bi_user_web")
@Data
public class UserBItem implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private Integer pid;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String name;

    @Column(name = "create_time", nullable = false)
    private Long createTime;

    @Column(nullable = false)
    private Integer state;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String liaisons;

    @Column(nullable = false)
    private String liaisonsPhone;

    @Column(name = "latest_login_time", nullable = false)
    private Long latestLoginTime;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "bi_user_role", joinColumns = @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT)),
    inverseJoinColumns = @JoinColumn(name = "role_id"), foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))

    private Set<RoleBItem> roles;

//    @Transient
//    private String roleName;

//    public void genarateRoleName() {
//        List<String> nameList = new ArrayList<>();
//        if (this.roles == null) {
//            return;
//        }
//        this.roles.forEach(role -> nameList.add(role.getName()));
//        this.roleName = String.join(",", nameList);
//        this.roles = null;
//    }
//
//    public Set<PermissionBItem> findPermission() {
//        Set<PermissionBItem> permissionSet = new HashSet<>();
//        if (this.roles != null) {
//            this.roles.forEach(role -> permissionSet.addAll(role.getPermissions()));
//        }
//        return permissionSet;
//    }

    public Boolean isSuper() {
        return Constants.SUPER_ADMIN.equals(this.username);
    }
}
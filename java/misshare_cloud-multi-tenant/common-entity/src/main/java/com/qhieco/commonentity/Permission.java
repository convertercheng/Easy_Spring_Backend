package com.qhieco.commonentity;

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
 * 对应权限表
 */
@Data
@Entity
@Table(name = "t_permission")
public class Permission {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true)
    private String name;

    @Column
    private String url;

    @Column
    private Long createTime;

    @Column
    private Integer state;

    /**
     通过位运算鉴定该等级的用户是否可分配相应权限,子账号对应第三位 4,父账号第二位 2,
     平台对应第一位 1。例如:权限 1 的 level 为 3,转为二进制数字为 011,证明平台和父账号可
     以被分配该权限。实际编码过程中可以采用位运算进行鉴定,例如 6&4=0,表明子账号不
     能分配该权限
     */
    @Column(nullable = false)
    private Integer allocation;
}
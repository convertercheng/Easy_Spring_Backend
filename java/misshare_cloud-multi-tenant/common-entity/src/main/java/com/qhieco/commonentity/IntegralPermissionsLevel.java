package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/5/24 14:12
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
@Entity
@Table(name = "t_integral_permissions_level")
public class IntegralPermissionsLevel {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "integral_permissions_remark")
    private String integralPermissionsRemark;

    @Column(name = "integral_permissions_start")
    private Integer integralPermissionsStart;

    @Column(name = "integral_permissions_end")
    private Integer integralPermissionsEnd;

    @Column(name = "integral_permissions_coefficient")
    private Double integralPermissionsCoefficient;

    @Column(name = "update_time")
    private Long updateTime;

    @Column(name = "state")
    private Integer state;
}

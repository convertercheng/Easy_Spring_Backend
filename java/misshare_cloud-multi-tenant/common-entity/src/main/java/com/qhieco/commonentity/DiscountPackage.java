package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/7/11 9:46
 * <p>
 * 类说明：
 * 对应web套餐表
 */
@Entity
@Table(name = "t_discount_package")
@Data
public class DiscountPackage implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private Integer type;

    @Column(name = "effective_daytime")
    private Long effectiveDaytime;
    
    @Column(name = "effective_begin_time")
    private Long effectiveBeginTime;

    @Column(name = "effective_end_time")
    private Long effectiveEndTime;

    @Column(name = "toplimit")
    private Integer toplimit;

    @Column(name = "info_rule")
    private Integer infoRule;

    @Column(name = "real_quantity")
    private Integer realQuantity;

    @Column(name = "package_amount")
    private Integer packageAmount;

    @Column(nullable = false)
    private Integer state;

    @Column(name = "descript")
    private String descript;

    @Column(name = "update_time")
    private Long update_time;

    @Column(name = "create_time")
    private Long createTime;


}
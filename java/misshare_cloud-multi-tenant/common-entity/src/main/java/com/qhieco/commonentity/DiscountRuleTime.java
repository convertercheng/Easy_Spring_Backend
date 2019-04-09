package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/7/11 9:46
 * <p>
 * 类说明：
 * 对应web套餐时段表
 */
@Entity
@Table(name = "t_rule_time")
@Data
public class DiscountRuleTime implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "discount_package_id")
    private String discountPackageId;

    @Column(name = "type")
    private Integer type;

    @Column(name = "begin_time")
    private Long beginTime;

    @Column(name = "end_time")
    private Long endTime;

    @Column(name = "package_amount")
    private Integer packageAmount;

    @Column(nullable = false)
    private Integer state;

    @Column(name = "update_time")
    private Long update_time;

    @Column(name = "create_time")
    private Long createTime;


}
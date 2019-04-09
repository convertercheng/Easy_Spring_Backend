package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/5/24 10:24
 * <p>
 * 类说明：
 * ${description}
 */
@Data
@Entity
@Table(name = "t_integral")
public class Integral {
    @Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;

    /**
     * 积分项说明
     */
    @Column(name = "integral_pluses_remark")
    private String integralPlusesRemark;

    /**
     * 积分
     */
    @Column(name = "integral_pluses")
    private Integer integralPluses;

    /**
     * 类型（1-加分，2减分）
     */
    @Column(name = "integral_type")
    private Integer integralType;

    /**
     * 加分或减分编码
     */
    @Column(name = "integral_code")
    private String integralCode;

    /**
     * 操作时间
     */
    @Column(name = "update_time")
    private Long updateTime;

    /**
     * 状态
     */
    @Column(name = "state")
    private Integer state;

}

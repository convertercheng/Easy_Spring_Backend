package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应道闸日志表
 */
@Entity
@Table(name = "t_log_barrier")
@Data
public class LogBarrier {

    @Id
    @GeneratedValue
    private Integer id;

    /**
     * 道闸id
     */
    @Column(nullable = false)
    private Integer barrierId;

    @Column
    private BigDecimal cost;

    /**
     *入场时间
     */
    @Column
    private Long enterTime;

    /**
     *离场时间
     */
    @Column
    private Long leaveTime;

    /**
     *暂时离场
     */
    @Column
    private Boolean tempLeave;

    /**
     *订单编号
     */
    @Column
    private Integer orderId;

    @Column
    private String plateNo;

    /**
     *入场编码
     */
    @Column
    private Integer uniqueEnterNo;

    /**
     *状态
     */
    @Column(nullable = false)
    private Integer state;
}
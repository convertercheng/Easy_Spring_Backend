package com.qhieco.commonentity;

import lombok.Data;

import javax.naming.Name;
import javax.persistence.*;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应停车区动态车位数量表
 */
@Data
@Entity
@Table(name = "t_parklot_amount")
public class ParklotAmount {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;

    /**
     * 停车区id
     */
    @Column(name = "parklot_id")
    private Integer parklotId;

    /**
     * 停车位总量
     */
    @Column(name = "total_amount")
    private Integer totalAmount;

    /**
     * 停车位签约总量
     */
    @Column(name = "signed_amount")
    private Integer signedAmount;

    /**
     * 停车位发布数量
     */
    @Column(name = "publish_amount")
    private Integer publishAmount;

    /**
     * 停车位闲置数量
     */
    @Column(name = "idle_amount")
    private Integer idleAmount;

    /**
     * 停车位已预约数量
     */
    @Column(name = "reserved_amount")
    private Integer reservedAmount;

    /**
     * 当前时间可预约的停车位数量
     */
    @Column(name = "reservable_amount")
    private Integer reservableAmount;

    /**
     * 继电器统计出来的剩余停车位数量
     */
    @Column(name = "left_amount")
    private Integer leftAmount;

    /**
     * 是否显示剩余停车位数量，0：不显示，1：显示
     */
    @Column(name = "left_amount_type")
    private Integer leftAmountType;

    /**
     * 修改时间
     */
    @Column(name = "modify_time")
    private Long modifyTime;

    /**
     * 继电器更新时间
     */
    @Column(name = "relay_update_time")
    private Long relayUpdateTime;

    public ParklotAmount() {
    }

    public ParklotAmount(Integer parklotId, Integer totalAmount, Integer signedAmount, Integer publishAmount, Integer idleAmount, Integer reservedAmount, Integer reservableAmount, Integer leftAmount, Integer leftAmountType, Long modifyTime, Long relayUpdateTime) {
        this.parklotId = parklotId;
        this.totalAmount = totalAmount;
        this.signedAmount = signedAmount;
        this.publishAmount = publishAmount;
        this.idleAmount = idleAmount;
        this.reservedAmount = reservedAmount;
        this.reservableAmount = reservableAmount;
        this.leftAmount = leftAmount;
        this.leftAmountType = leftAmountType;
        this.modifyTime = modifyTime;
        this.relayUpdateTime = relayUpdateTime;
    }

    @Override
    public String toString() {
        return "ParklotAmount{" +
                "id=" + id +
                ", parklotId=" + parklotId +
                ", totalAmount=" + totalAmount +
                ", signedAmount=" + signedAmount +
                ", publishAmount=" + publishAmount +
                ", idleAmount=" + idleAmount +
                ", reservedAmount=" + reservedAmount +
                ", reservableAmount=" + reservableAmount +
                ", leftAmount=" + leftAmount +
                ", leftAmountType=" + leftAmountType +
                ", modifyTime=" + modifyTime +
                ", relayUpdateTime=" + relayUpdateTime +
                '}';
    }
}
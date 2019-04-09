package com.qhieco.commonentity.relational;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应停车区收费规则关联表
 */
@Data
@Entity
@Table(name = "b_parklot_fee_rule_parking")
public class ParklotFeeRuleParkingB {

    public ParklotFeeRuleParkingB() {
    }

    public ParklotFeeRuleParkingB(Integer parklotId, Integer feeRuleId, Integer state, Long updateTime) {
        this.parklotId = parklotId;
        this.feeRuleId = feeRuleId;
        this.state = state;
        this.updateTime = updateTime;
    }

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "parklot_id")
    private Integer parklotId;

    @Column(name = "fee_rule_id")
    private Integer feeRuleId;

    @Column(name = "state")
    private Integer state;

    @Column(name = "update_time")
    private Long updateTime;
}
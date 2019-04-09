package com.qhieco.commonentity;

import lombok.Data;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应费用规则表
 */
@Data
@Entity
@Table(name = "t_fee_rule_parking")
public class FeeRuleParking {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;

    @Column(name = "fee_rule_parking_base_id")
    private Integer feeRuleParkingBaseId;

    @Column(name = "start_time")
    private Long startTime;

    @Column(name = "end_time")
    private Long endTime;

    @Column(name = "first_hour_fee", nullable = false)
    private BigDecimal firstHourFee;

    @Column(name = "other_time_period", nullable = false)
    private Integer otherTimePeriod;

    @Column(name = "other_time_fee", nullable = false)
    private BigDecimal otherTimeFee;

    @Column(name = "max_fee", nullable = false)
    private BigDecimal maxFee;

    @Column(name = "free_use_time", nullable = false)
    private Integer freeUseTime;

    @Column(name = "over_time_fee", nullable = false)
    private BigDecimal overTimeFee;

    @Column(name = "weekday", nullable = false)
    private Integer weekday;

    @Column(name = "remark")
    private String remark;

    @Column(name = "update_time")
    private Long updateTime;

    @Column(name = "state", nullable = false)
    private Integer state;


    public FeeRuleParking() {
    }

    public FeeRuleParking(Integer feeRuleParkingBaseId,Long startTime,Long endTime, BigDecimal firstHourFee,Integer otherTimePeriod, BigDecimal otherTimeFee, BigDecimal maxFee, Integer freeUseTime, BigDecimal overTimeFee, Integer weekday, Integer state) {
        this.feeRuleParkingBaseId = feeRuleParkingBaseId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.firstHourFee = firstHourFee;
        this.otherTimePeriod = otherTimePeriod;
        this.otherTimeFee = otherTimeFee;
        this.maxFee = maxFee;
        this.freeUseTime = freeUseTime;
        this.overTimeFee = overTimeFee;
        this.weekday = weekday;
        this.state = state;
    }
}
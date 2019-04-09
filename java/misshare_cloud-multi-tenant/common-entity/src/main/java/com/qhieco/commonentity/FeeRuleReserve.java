package com.qhieco.commonentity;

import com.qhieco.TenantSupport;
import lombok.Data;
import org.hibernate.annotations.Filter;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应费用规则表
 */
@Data
@Entity
@Table(name = "t_fee_rule_reserve")
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class FeeRuleReserve implements TenantSupport {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "fee", nullable = false)
    private String fee;

    @Column(name = "finish_time", nullable = false)
    private String finishTime;

    @Column(name = "state")
    private Integer state;

    @Column
    private Integer tenantId;

    @Transient
    private String tenantName;

    public FeeRuleReserve() {
    }

    public FeeRuleReserve(String name, String fee, String finishTime, Integer state) {
        this.name = name;
        this.fee = fee;
        this.finishTime = finishTime;
        this.state = state;
    }

    @Override
    public String toString() {
        return "FeeRuleReserve{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fee='" + fee + '\'' +
                ", finishTime='" + finishTime + '\'' +
                ", state=" + state +
                '}';
    }
}
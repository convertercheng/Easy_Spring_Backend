package com.qhieco.commonentity;

import com.qhieco.TenantSupport;
import lombok.Data;
import org.hibernate.annotations.Filter;

import javax.persistence.*;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/6/25 19:33
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
@Entity
@Table(name = "t_fee_rule_parking_base")
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class FeeRuleParkingBase implements TenantSupport {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private Integer type;

    @Column(name = "create_time")
    private Long createTime;

    @Column(name = "state")
    private Integer state;

    @Column
    private Integer tenantId;

    @Transient
    private String tenantName;

    public FeeRuleParkingBase(){}

    public FeeRuleParkingBase(String name,Integer type,Long createTime,Integer state){
        this.name = name;
        this.type = type;
        this.createTime = createTime;
        this.state = state;
    }

}

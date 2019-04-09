package com.qhieco.commonentity;

import com.qhieco.TenantSupport;
import lombok.Data;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_parklot_district")
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class ParklotDistrict implements TenantSupport{

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;

    /**
     * 区域名称
     */
    @Column(name = "district_name")
    private String districtName;

    /**
     * 停车场ID
     */
    @Column(name = "parklot_id")
    private Integer parklotId;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Long updateTime;

    /**
     * 状态
     */
    @Column(name = "state")
    private Integer state;

    @Column
    private Integer tenantId;

    @Transient
    private String parkLotName;

    @Transient
    private String parkLotTypeStr;

    @Transient
    private String tenantName;
}

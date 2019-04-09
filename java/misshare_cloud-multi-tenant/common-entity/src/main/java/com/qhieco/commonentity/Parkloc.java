package com.qhieco.commonentity;

import com.qhieco.TenantSupport;
import lombok.Data;
import org.hibernate.annotations.Filter;

import javax.persistence.*;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应停车位表
 */
@Data
@Entity
@Table(name = "t_parkloc")
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class Parkloc implements TenantSupport{
    @Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;

    /**
     * 车位编号
     */
    @Column(name = "number", nullable = false)
    private String number;

    /**
     * 车位区域ID
     */
    @Column(name = "parklot_district_id")
    private Integer parklotDistrictId;

    /**
     * 停车区id
     */
    @Column(name = "parklot_id")
    private Integer parklotId;

    /**
     * 手机用户Id
     */
    @Column(name = "mobile_user_id", nullable = false)
    private Integer mobileUserId;

    /**
     * 更新时间
     */
    @Column(name = "update_time", nullable = false)
    private Long updateTime;

    /**
     * 状态
     */
    @Column(name = "state", nullable = false)
    private Integer state;

    @Column
    private Integer tenantId;

    public Parkloc(){}

    public Parkloc(String number, Integer parklotId, Integer mobileUserId, Long updateTime, Integer state) {
        this.number = number;
        this.parklotId = parklotId;
        this.mobileUserId = mobileUserId;
        this.updateTime = updateTime;
        this.state = state;
    }

    public Parkloc(String number, Integer parklotId, Integer mobileUserId, Long updateTime,Integer parklotDistrictId, Integer state) {
        this.number = number;
        this.parklotId = parklotId;
        this.mobileUserId = mobileUserId;
        this.updateTime = updateTime;
        this.state = state;
        this.parklotDistrictId=parklotDistrictId;
    }

    @Transient
    private String parklotTypeStr;

    @Transient
    private String parklotDistrictName;

    @Transient
    private String tenantName;
}
package com.qhieco.commonentity.iotdevice;

import com.qhieco.constant.Status;
import lombok.Data;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-13 下午5:24
 * <p>
 * 类说明：
 * ${description}
 */
@Data
@Table(name = "t_lock")
@Entity
//@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "string")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class Lock extends AbstractIotDevice {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String btName;

    @Column(nullable = false)
    private String btPassword;

    @Column(nullable = false)
    private String serialNumber;

    @Column(name = "bt_mac", nullable = false)
    private String mac;

    @Column
    private Integer gatewayId;

    @Column
    private Integer parklocId;

    @Column(nullable = false)
    private Double battery;

    @Column(name = "update_time")
    private Long updateTime;

    @Column(name = "state")
    private Integer state;

    @Column(name = "manufacturer_name")
    private String manufacturerName;

    @Column(name = "model")
    private String model;

    @Column
    private Integer type;

    @Transient
    private String parklotName;

    @Transient
    private Integer parklotType;

    @Transient
    private String parklocNumber;

    @Transient
    private String identifier;

    @Transient
    private String parklotTypeStr;

    @Transient
    private String stateStr;

    @Transient
    private Integer innershare;

    @Transient
    private String innershareStr;

    @Transient
    private String typeStr;


    @Override
    public void setForeignKey(Integer value) {
        setParklocId(value);
    }

    public String getStateStr(){
        return Status.Common.find(this.getState());
    }

    public String getTypeStr() {
        String typeStr = "";
        if (this.type != null) {
            typeStr = Status.Lock.find(this.type);
        }
        return typeStr;
    }
}
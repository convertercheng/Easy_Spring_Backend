package com.qhieco.commonentity.iotdevice;

import lombok.Data;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-13 下午4:02
 * <p>
 * 类说明：
 * ${description}
 */
@Data
@Table(name = "t_access")
@Entity
//@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "string")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class Access extends AbstractIotDevice {

    private static final long serialVersionUID = 1L;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String intro;

    @Column(name = "bt_name", nullable = false)
    private String btName;

    @Column(name = "bt_pwd", nullable = false)
    private String btPwd;

    @Column(name = "create_time", nullable = false)
    private Long createTime;

    @Column
    private Integer parklotId;

    @Transient
    private String parklotName;

    @Transient
    private Integer parklotType;

    @Transient
    private String parklotTypeStr;

    @Transient
    private Integer innershare;

    @Transient
    private String innershareStr;

    @Override
    public void setForeignKey(Integer value) {
        setParklotId(value);
    }
}

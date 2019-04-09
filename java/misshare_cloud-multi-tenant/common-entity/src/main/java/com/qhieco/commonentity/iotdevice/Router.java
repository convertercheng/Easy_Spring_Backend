package com.qhieco.commonentity.iotdevice;

import com.qhieco.response.data.web.AbstractPaged;
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
 * @version 2.0.1 创建时间: 18-3-21 下午4:36
 * <p>
 * 类说明：
 * ${description}
 */
@Data
@Table(name = "t_router")
@Entity
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class Router extends AbstractIotDevice {
    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    private String name;

    @Column
    private Integer parklotId;

    @Transient
    private String parklotName;

    @Transient
    private AbstractPaged<Gateway> gatewayList;

    @Override
    public void setForeignKey(Integer value) {
        this.parklotId = value;
    }
}

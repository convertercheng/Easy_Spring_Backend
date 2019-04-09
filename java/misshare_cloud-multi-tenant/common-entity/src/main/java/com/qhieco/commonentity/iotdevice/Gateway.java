package com.qhieco.commonentity.iotdevice;

import com.qhieco.commonentity.Area;
import lombok.Data;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应网关表
 */
@Data
@Table(name = "t_gateway")
@Entity
//@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "string")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class Gateway extends AbstractIotDevice {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String identifier;

    @Column
    private Integer routerId;

    @Transient
    private List<Area> areaList;

    @Transient
    private String parklotName;

    @Transient
    private Integer lockNum;

    @Override
    public void setForeignKey(Integer value) {
        this.routerId = value;
    }

    @Transient
    private String routerName;
}
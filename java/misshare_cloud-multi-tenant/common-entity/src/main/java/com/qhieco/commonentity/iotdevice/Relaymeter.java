package com.qhieco.commonentity.iotdevice;

import lombok.Data;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应中继器表
 */
@Data
@Table(name = "t_relaymeter")
@Entity
//@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "string")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class Relaymeter extends AbstractIotDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    private String name;

    @Column
    private Integer parklotId;

    @Column
    private String number;

    @Column
    private Integer type;

    @Transient
    private String parklotName;

    @Override
    public void setForeignKey(Integer value) {
        setParklotId(value);
    }
}
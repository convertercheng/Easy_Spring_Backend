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
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 道闸表
 */
@Entity
@Table(name = "t_barrier")
@Data
//@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "string")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class Barrier extends AbstractIotDevice {

    @Column(nullable = false)
    private String name;

    /**
     * 道闸系统用户名
     */
    @Column
    private String username;

    /**
     * 道闸系统密码
     */
    @Column(nullable = false)
    private String password;

    /**
     *　停车场id
     */
    @Column
    private Integer parklotId;

    /**
     * 道闸服务器ip
     */
    @Column(nullable = false)
    private String serverIp;

    /**
     *道闸服务端口
     */
    @Column(nullable = false)
    private Integer serverPort;

    /**
     *
     */
    @Column
    private String url;

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
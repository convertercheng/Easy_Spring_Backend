package com.qhieco.commonentity.iotdevice;

import com.qhieco.TenantSupport;
import lombok.Data;
import lombok.val;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-13 下午3:29
 * <p>
 * 类说明：
 * ${description}
 */
@Data
@MappedSuperclass
public abstract class AbstractIotDevice implements Serializable,TenantSupport {

    /**
     *设备id
     **/
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Integer id;

    /**
     * 设备状态，0:无效,1:有效
     */
    @Column(nullable = false)
    protected Integer state;

    /**
     * 更新时间
     */
    @Column(nullable = false)
    protected Long updateTime;

    /**
     *型号
     */
    @Column
    private String model;

    /**
     *供应商名
     */
    @Column
    private String manufacturerName;

    @Column
    private Integer tenantId;

    @Transient
    private String tenantName;

    /**
     * 绑定外键操作
     * @param value
     */
    public abstract void setForeignKey(Integer value);

}

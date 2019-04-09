package com.qhieco.commonentity;

import com.qhieco.TenantSupport;
import lombok.Data;
import org.hibernate.annotations.Filter;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应退款订单表
 */
@Data
@Entity
@Table(name = "t_order_refund")
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class OrderRefund implements TenantSupport {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "order_parking_id")
    private Integer orderParkingId;

    @Column(name = "trade_no")
    private String tradeNo;

    @Column(name = "fee")
    private BigDecimal fee;

    @Column(name = "state")
    private Integer state;

    @Column(name = "create_time")
    private Long createTime;

    @Column(name = "update_time")
    private Long updateTime;

    @Column(name = "channel")
    private Integer channel;

    @Column
    private Integer tenantId;

    @Transient
    private String tenantName;

    @Transient
    private String stateStr;

    @Transient
    private String channelStr;

    @Transient
    private String phone;

    public OrderRefund(){}

    public OrderRefund(String serialNumber,Integer orderParkingId,String tradeNo,BigDecimal fee,Integer state,Long createTime,Long updateTime,Integer channel){
        this.serialNumber = serialNumber;
        this.orderParkingId = orderParkingId;
        this.tradeNo = tradeNo;
        this.fee = fee;
        this.state = state;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.channel = channel;
    }
}

package com.qhieco.commonentity;

import com.qhieco.constant.Constants;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 订单总表
 */
@Data
@Entity
@Table(name = "t_order_total")
public class OrderTotal {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "mobile_user_id")
    private Integer mobileUserId;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "type")
    private Integer type;

    @Column(name = "update_time")
    private Long updateTime;

    @Column(name = "account")
    private BigDecimal account;

    @Column(name = "state")
    private Integer state;

    public OrderTotal(){}

    public OrderTotal(Integer mobileUserId,String serialNumber,Integer type,Long updateTime,BigDecimal account,Integer state){
        this.mobileUserId = mobileUserId;
        this.serialNumber = serialNumber;
        this.type = type;
        this.updateTime = updateTime;
        this.account = account;
        this.state = state;
        if(null == account){
            account = Constants.BIGDECIMAL_ZERO;
        }
    }
}
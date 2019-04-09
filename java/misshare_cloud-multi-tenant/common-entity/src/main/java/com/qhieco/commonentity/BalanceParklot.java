package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 停车区余额表
 */
@Data
@Entity
@Table(name = "t_balance_parklot")
public class BalanceParklot {
    @Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;

    @Column(name = "parklot_id")
    private Integer parklotId;

    @Column(name = "balance")
    private BigDecimal balance;


}
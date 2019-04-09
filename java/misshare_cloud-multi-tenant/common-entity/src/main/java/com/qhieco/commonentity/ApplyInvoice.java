package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 发票申请表
 */

@Entity
@Table(name = "t_apply_invoice")
@Data
public class ApplyInvoice {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "mobile_user_id",nullable = false)
    private Integer mobileUserId;

    @Column(name = "web_user_id")
    private Integer webUserId;

    @Column(name = "taxpayer_id",nullable = false)
    private String taxpayerId;

    @Column(name = "file_id")
    private Integer fileId;

    @Column(name = "order_ids",nullable = false)
    private String orderIds;

    @Column(name = "title",nullable = false)
    private String title;

    @Column(name = "content",nullable = false)
    private String content;

    @Column(name = "fee",nullable = false)
    private BigDecimal fee;
    /**
     * 0是个人 1是企业
     */
    @Column(name = "type",nullable = false)
    private Integer type;

    @Column(name = "email",nullable = false)
    private String email;

    @Column(name = "message")
    private String message;

    @Column(name = "apply_time",nullable = false)
    private Long applyTime;

    @Column(name = "complete_time")
    private Long completeTime;

    @Column(name = "state",nullable = false)
    private Integer state;

    @Transient
    private String phone;

    public ApplyInvoice(){}

    public ApplyInvoice(Integer mobileUserId,String taxpayerId,String orderIds,String title,
                        String content,BigDecimal fee,Integer type,String email,Long applyTime,Integer state){
        this.mobileUserId = mobileUserId;
        this.taxpayerId = taxpayerId;
        this.orderIds = orderIds;
        this.title = title;
        this.content = content;
        this.fee = fee;
        this.type = type;
        this.email = email;
        this.applyTime = applyTime;
        this.state = state;
    }

    @Transient
    private String stateStr;
    @Transient
    private String typeStr;

}
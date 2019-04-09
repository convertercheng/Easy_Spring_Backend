package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 17:46
 * <p>
 * 类说明：
 * 发票表
 */

@Entity
@Table(name = "t_invoice")
@Data
public class Invoice {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "code")
    private String code;

    @Column(name = "number")
    private String number;

    @Column(name = "apply_id")
    private Integer applyId;

    @Column(name = "state")
    private Integer state;

    @Column(name = "create_time")
    private Long createTime;

    public Invoice(){}

    public Invoice(String code,String number,Integer applyId,Integer state,Long createTime){
        this.code = code;
        this.number = number;
        this.applyId = applyId;
        this.state = state;
        this.createTime = createTime;
    }

}
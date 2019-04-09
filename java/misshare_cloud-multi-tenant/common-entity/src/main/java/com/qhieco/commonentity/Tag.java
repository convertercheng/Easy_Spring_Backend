package com.qhieco.commonentity;

import com.qhieco.constant.Status;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应标签表
 */
@Data
@Entity
@Table(name = "t_tag")
public class Tag {

    public Tag() {
    }

    public Tag(String name, String comment, Long createTime, Long modifyTime, Integer userType, Long startSignupTime, Long endSignupTime, Integer orderNumber, BigDecimal orderAmount, Integer unsigninDays, Integer type, Integer state) {
        this.name = name;
        this.comment = comment;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
        this.userType = userType;
        this.startSignupTime = startSignupTime;
        this.endSignupTime = endSignupTime;
        this.orderNumber = orderNumber;
        this.orderAmount = orderAmount;
        this.unsigninDays = unsigninDays;
        this.type = type;
        this.state = state;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "comment")
    private String comment;

    @Column(name = "create_time")
    private Long createTime;

    @Column(name = "modify_time")
    private Long modifyTime;

    @Column(name = "user_type")
    private Integer userType;

    @Column(name = "start_signup_time")
    private Long startSignupTime;

    @Column(name = "end_signup_time")
    private Long endSignupTime;

    @Column(name = "order_number")
    private Integer orderNumber;

    @Column(name = "order_amount")
    private BigDecimal orderAmount;

    @Column(name = "unsignin_days")
    private Integer unsigninDays;

    @Column(name = "type")
    private Integer type;

    @Column(name = "state")
    private Integer state;

    @Transient
    private String typeStr;

    public String getTypeStr() {
        String str = "";
        if (type != null) {
            str = Status.TagType.find(this.type);
        }
        return str;
    }
}
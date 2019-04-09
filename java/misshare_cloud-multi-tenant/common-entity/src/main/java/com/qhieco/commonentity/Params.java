package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应参数表
 */
@Data
@Entity
@Table(name = "t_params")
public class Params {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "qhkey")
    private String qhKey;

    @Column(name = "qhvalue")
    private String qhValue;

    @Column(name = "level")
    private Byte level;

    @Column(name = "state")
    private Integer state;
}
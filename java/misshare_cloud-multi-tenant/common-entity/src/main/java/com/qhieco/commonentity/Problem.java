package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 问题表
 */
@Data
@Entity
@Table(name = "t_problem")
public class Problem {
    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String proIntro;

    @Column
    private Long createTime;

    @Column
    private Integer state;
}
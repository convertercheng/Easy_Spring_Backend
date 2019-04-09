package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/12 18:03
 * <p>
 * 类说明：
 * 注册来源标记实体类
 */

@Entity
@Table(name = "t_register")
@Data
public class Register {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "source")
    private String source;

    @Column(name = "medium")
    private String medium;

    @Column(name = "content")
    private String content;

    @Column(name = "keyword")
    private String keyword;

    @Column(name = "series")
    private String series;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "pv")
    private Integer pv;

    @Column(name = "uv")
    private Integer uv;

    @Column(name = "remark")
    private String remark;

    @Column(name = "state")
    private Integer state;
}

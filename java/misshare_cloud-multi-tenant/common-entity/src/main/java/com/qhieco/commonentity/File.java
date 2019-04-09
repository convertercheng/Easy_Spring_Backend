package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应文件表
 */
@Data
@Entity
@Table(name = "t_file")
public class File {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;

    /**
     * 文件名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 文件路径
     */
    @Column(name = "path")
    private String path;

    /**
     * 文件说明
     */
    @Column(name = "intro")
    private String intro;

    /**
     * 文件创建时间
     */
    @Column(name = "createTime")
    private Long createTime;

    /**
     * 状态
     */
    @Column(name = "state")
    private Integer state;

    public File(){}

    public File(String name,String path,String intro,Long createTime,Integer state){
        this.name = name;
        this.path = path;
        this.intro = intro;
        this.createTime = createTime;
        this.state = state;
    }

    public File(Integer id, String name,String path,String intro,Long createTime,Integer state){
        this.id = id;
        this.name = name;
        this.path = path;
        this.intro = intro;
        this.createTime = createTime;
        this.state = state;
    }
}
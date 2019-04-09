package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应升级表
 */
@Data
@Entity
@Table(name = "t_update")
public class Update {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;

    @Column(name = "vcode")
    private String vcode;

    @Column(name = "updateinfo")
    private String updateinfo;

    @Column(name = "forceupdate")
    private String forceupdate;

    @Column(name = "type")
    private Integer type;

    @Column(name = "create_time")
    private Long createTime;

    @Column(name = "state")
    private Integer state;

    public Update(){}

    public Update(String vcode,String updateinfo,String forceupdate,Integer type,Long createTime,Integer state){
        this.vcode = vcode;
        this.updateinfo = updateinfo;
        this.forceupdate = forceupdate;
        this.type = type;
        this.createTime = createTime;
        this.state = state;
    }
}
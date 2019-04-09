package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.*;
import java.util.HashMap;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应消息表
 */
@Entity
@Table(name = "t_message")
@Data
public class Message {

    public Message(){}

    public Message(Integer mobileUserId,Integer webUserId,String title,String content,String href,Integer type,Integer kind,Integer state,Long createTime){
        this.mobileUserId = mobileUserId;
        this.webUserId = webUserId;
        this.title = title;
        this.content = content;
        this.href = href;
        this.type = type;
        this.kind = kind;
        this.state = state;
        this.createTime = createTime;
    }

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "mobile_user_id")
    private Integer mobileUserId;

    @Column(name = "web_user_id")
    private Integer webUserId;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "href")
    private String href;

    @Column(name = "type")
    private Integer type;

    @Column(name = "kind")
    private Integer kind;

    @Column(name = "state")
    private Integer state;

    @Column(name = "create_time")
    private Long createTime;

    @Transient
    private String phone;

    @Transient
    private String TypeAndKind;


}
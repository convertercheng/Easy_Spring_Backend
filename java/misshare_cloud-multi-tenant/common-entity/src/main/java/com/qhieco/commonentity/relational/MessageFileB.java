package com.qhieco.commonentity.relational;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应消息文件关联表
 */
@Data
@Entity
@Table(name = "b_message_file")
public class MessageFileB {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;

    /**
     * 消息id
     */
    @Column(name = "message_id")
    private Integer messageId;

    /**
     * 文件id
     */
    @Column(name = "file_id")
    private Integer fileId;

    /**
     * 状态
     */
    @Column(name = "state")
    private Integer state;


    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Long updateTime;

    public MessageFileB() {
    }

    public MessageFileB(Integer messageId, Integer fileId, Integer state, Long updateTime) {
        this.messageId = messageId;
        this.fileId = fileId;
        this.state = state;
        this.updateTime = updateTime;
    }
}
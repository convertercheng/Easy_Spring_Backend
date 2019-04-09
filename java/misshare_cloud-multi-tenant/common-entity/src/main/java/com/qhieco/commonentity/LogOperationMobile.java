package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应移动用户操作日志表
 */
@Data
@Entity
@Table(name = "t_log_operation_mobile")
public class LogOperationMobile {

    public LogOperationMobile(){

    }

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;

    @Column(name = "mobile_user_id")
    private Integer mobileUserId;

    @Column(name = "content")
    private String content;

    @Column(name = "source_ip")
    private String sourceIp;

    @Column(name = "source_model")
    private String sourceModel;

    @Column(name = "type")
    private Integer type;

    @Column(name = "operate_time")
    private Long operateTime;

}
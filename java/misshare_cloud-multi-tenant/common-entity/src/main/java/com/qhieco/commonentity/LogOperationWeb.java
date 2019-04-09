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
 * 对应Web用户操作日志表
 */
@Data
@Entity
@Table(name = "t_log_operation_web")
public class LogOperationWeb {
    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private Integer webUserId;

    @Column
    private String content;

    @Column
    private String sourceIp;

    @Column
    private Long operateTime;

    @Column
    private Integer state;
}
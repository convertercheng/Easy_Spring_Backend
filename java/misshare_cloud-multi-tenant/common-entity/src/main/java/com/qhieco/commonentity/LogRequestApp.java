package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/22 下午8:34
 * <p>
 * 类说明：
 *     请求日志实体类
 */
@Data
@Entity
@Table(name = "t_log_request_app")
public class LogRequestApp {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String method;

    @Column(nullable = false)
    private String uri;

    @Column(nullable = false)
    private Integer times;

    @Column(name = "success_times", nullable = false)
    private Integer successTimes;

}

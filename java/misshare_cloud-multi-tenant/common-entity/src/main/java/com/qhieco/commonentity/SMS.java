package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/2/26 下午3:19
 * <p>
 * 类说明：
 *     对应t_sms
 */
@Data
@Entity
@Table(name = "t_sms")
public class SMS {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "phone_num")
    private String phone;

    @Column(nullable = false)
    private String content;

    @Column
    private Byte type;

    @Column
    private Integer state;

    @Column(name = "create_time")
    private Long createTime;

    public SMS() {}

    public SMS(String phone, String content, Byte type, Integer state, Long createTime) {
        this.phone = phone;
        this.content = content;
        this.type = type;
        this.state = state;
        this.createTime = createTime;
    }
}

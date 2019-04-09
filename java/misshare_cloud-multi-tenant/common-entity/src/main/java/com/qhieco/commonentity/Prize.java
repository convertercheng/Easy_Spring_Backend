package com.qhieco.commonentity;

import com.qhieco.constant.Status;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/6/12 11:26
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
@Entity
@Table(name = "t_prize")
public class Prize {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private Integer type;

    @Column(name = "money")
    private BigDecimal money;

    @Column(name = "intro")
    private String intro;

    @Column(name = "file_id")
    private Integer fileId;

    @Column(name = "start_time")
    private Long startTime;

    @Column(name = "end_time")
    private Long endTime;

    @Column(name = "create_time")
    private Long createTime;

    @Column(name = "modify_time")
    private Long modifyTime;

    @Column(name = "state")
    private Integer state;

    @Transient
    private String typeStr;

    public String getTypeStr() {
        return Status.PrizeType.find(this.type);
    }
}

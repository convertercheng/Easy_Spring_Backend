package com.qhieco.commonentity;

import com.qhieco.constant.Status;
import lombok.Data;

import javax.persistence.*;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/13 19:33
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
@Entity
@Table(name = "t_activity")
public class Activity {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "intro")
    private String intro;

    @Column(name = "begin_time")
    private Long beginTime;

    @Column(name = "end_time")
    private Long endTime;

    @Column(name = "type")
    private Integer type;

    @Column(name = "href")
    private String href;

    @Column(name = "create_time")
    private Long createTime;

    /**
     * 状态：1有效 0无效
     * 当时间在beginTime和endTime之间并且state=1时，活动为进行中
     */
    @Column(name = "state")
    private Integer state;

    @Transient
    private String typeStr;
    @Transient
    private String stateStr;

    public String getStateStr() {
        return Status.Common.find(this.state);
    }

    public String getTypeStr() {
        return Status.ActivityType.find(this.type);
    }

    public Activity() {
    }

    public Activity(String name, String intro, Long beginTime, Long endTime, Integer type, String href, Long createTime, Integer state) {
        this.name = name;
        this.intro = intro;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.type = type;
        this.href = href;
        this.createTime = createTime;
        this.state = state;
    }
}

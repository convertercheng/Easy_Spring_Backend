package com.qhieco.commonentity.relational;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/13 19:43
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
@Entity
@Table(name = "b_activity_file")
public class ActivityFileB {
    @Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;

    @Column(name = "activity_id")
    private Integer activityId;

    @Column(name = "file_id")
    private Integer fileId;

    /**
     * 1：长图 首页显示， 2：宽图 列表显示
     * 参考status.ActivityFileState
     */
    @Column(name = "state")
    private Integer state;
}

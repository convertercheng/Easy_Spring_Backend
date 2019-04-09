package com.qhieco.commonentity;

import com.qhieco.constant.Status;
import lombok.Data;

import javax.persistence.*;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/6/13 10:33
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
@Entity
@Table(name = "t_statistics")
public class Statistics {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;

    @Column(name = "activity_id")
    private Integer activityId;

    /**
     * 统计类型(1:浏览量，2:参与人数，3:获奖人数，4:活动触发)
     */
    @Column(name = "type")
    private Integer type;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "value")
    private Integer value;

    @Column(name = "create_time")
    private Long createTime;

    public Statistics() {
    }

    public Statistics(Integer activityId, Integer type,Integer userId, Integer value, Long createTime) {
        this.activityId = activityId;
        this.type = type;
        this.userId = userId;
        this.value = value;
        this.createTime = createTime;
    }
}

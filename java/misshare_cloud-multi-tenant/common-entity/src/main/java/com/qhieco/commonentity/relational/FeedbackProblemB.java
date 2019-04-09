package com.qhieco.commonentity.relational;

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
 * 对应反馈问题联合表
 */
@Data
@Entity
@Table(name = "b_feedback_problem")
public class FeedbackProblemB {

    public FeedbackProblemB(){}

    public FeedbackProblemB(Integer feedbackId,Integer problemId,Long createTime,Long modifyTime,Integer state){
        this.feedbackId = feedbackId;
        this.problemId = problemId;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
        this.state = state;
    }

    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private Integer feedbackId;

    @Column
    private Integer problemId;

    @Column
    private Long createTime;

    @Column
    private Long modifyTime;

    @Column
    private Integer state;
}
package com.qhieco.commonentity.relational;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应停车区文件关联表
 */
@Data
@Entity
@Table(name = "b_parklot_file")
public class ParklotFileB {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;

    /**
     * 停车区id
     */
    @Column(name = "parklot_id")
    private Integer parklotId;

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
     * 创建时间
     */
    @Column(name = "create_time")
    private Long createTime;

    /**
     * 修改时间
     */
    @Column(name = "modify_time")
    private Long modifyTime;

    public ParklotFileB() {
    }

    public ParklotFileB(Integer parklotId, Integer fileId, Integer state, Long createTime, Long modifyTime) {
        this.parklotId = parklotId;
        this.fileId = fileId;
        this.state = state;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
    }
}
package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.*;


/**
 * 用户与车牌套餐绑定实体类
 */
@Data
@Entity
@Table(name = "t_user_package")
public class userPackage {
    @Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;

    @Column(name = "mobile_user_id")
    private Integer mobileUserId;

    @Column(name = "package_id")
    private Integer packageId;

    @Column(name = "plate_id")
    private Integer plateId;

    @Column(name = "parklot_id")
    private Integer parklotId;

    @Column(name = "real_start_time")
    private Long realStartTime;

    @Column(name = "real_end_time")
    private Long realEndTime;

    @Column(name = "state")
    private Integer state;

    @Column(name = "create_time")
    private Long createTime;
}

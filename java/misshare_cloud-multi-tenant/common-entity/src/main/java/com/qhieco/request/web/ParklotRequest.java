package com.qhieco.request.web;

import lombok.Data;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-26 下午8:04
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class ParklotRequest extends QueryPaged {

    private String name;
    private Integer type;
    private String id;
    private Integer provinceId;
    private Integer cityId;
    private Integer areaId;
    private Integer innershare;

    private Integer state;
    private Integer userId;

    private Integer parkId; // 停车场ID
    private Integer adminId; // 停车场管理员ID
    private String adminName; // 管理员名称
    private String adminPhone; // 管理员联系方式
    private Integer applyState; // 工单状态

}

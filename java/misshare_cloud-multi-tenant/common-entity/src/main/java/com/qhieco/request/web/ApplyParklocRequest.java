package com.qhieco.request.web;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/29 下午7:53
 * <p>
 * 类说明：
 *     车位申请审批请求
 */
@Data
public class ApplyParklocRequest extends  QueryPaged{

    private Integer applyId;

    private Integer webUserId;

    /**
     * 状态
     * 1700 处理中
     * 1701 通过
     * 1702 不通过
     */
    private Integer state;

    private String message;

    private String areaName;

    private String  parklotName;

    private String phone;

    private String contactPhone;

    private  Long startApplyTime;

    private  Long endApplyTime;

    private Integer provinceId;
    private Integer cityId;
    private Integer areaId;
    private List<Integer> ids;


}

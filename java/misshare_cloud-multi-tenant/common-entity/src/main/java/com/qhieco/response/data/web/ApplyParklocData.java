package com.qhieco.response.data.web;

import lombok.Data;

import java.util.HashMap;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/3/30 11:45
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class ApplyParklocData {

    /**
     * ID
     */
    private Integer id;

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 小区名称
     */
    private  String parklotName;

    /**
     * 用户手机
     */
    private  String userPhone;

    /**
     * 联系电话
     */
    private  String  contactPhone;

    /**
     * 申请日期
     */
    private  Long applyTime;

    /**
     * 处理完成时间
     */
    private  Long completeTime;

    /**
     * 状态
     */
    private  Integer state;

    /**
     * 状态值
     */
    private String stateStr;


}

package com.qhieco.request.web;

import lombok.Data;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/6/15 14:52
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class ManualTagSaveRequest {
    /**
     * 标签id
     */
    private Integer tagId;

    /**
     * 标签名称
     */
    private String tagName;
    /**
     * 标签描述
     */
    private String comment;
    /**
     * 用户id列表
     */
    List<Integer> userIds;
}

package com.qhieco.response.data.web;

import lombok.Data;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/6/21 15:43
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class TagManualData {

    private Integer tagId;
    private String name;
    private String comment;

    /**
     * 已绑定的用户列表
     */
    private List<Integer> bindUsers;

    private List<TagUserListData> tagUserListDataList;
}

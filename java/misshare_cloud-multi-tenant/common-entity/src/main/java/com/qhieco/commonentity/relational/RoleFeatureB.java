package com.qhieco.commonentity.relational;

import lombok.Data;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应角色权限模块关联表
 */
@Data
public class RoleFeatureB {
    private Integer id;

    private Integer roleId;

    private Integer featureId;

    private Integer state;

    private Long createTime;

    private Long modifyTime;
}
package com.qhieco.commonentity;

import lombok.Data;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应运营商表
 */
@Data
public class Operator {
    private Integer id;

    private String name;

    private String phone;

    private Long createTime;

    private Long modifyTime;

    private Integer state;
}
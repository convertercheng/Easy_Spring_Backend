package com.qhieco.response.data.web;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/22 下午6:11
 * <p>
 * 类说明：
 * ${description}
 */
@Setter
@Getter
public class AreaData {

    public AreaData(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    private Integer id;

    private String name;

    @Override
    public String toString() {
        return "AreaData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

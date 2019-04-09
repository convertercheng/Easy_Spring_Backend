package com.qhieco.request.web;

import lombok.Data;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-14 下午4:10
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class AccessRequest extends QueryPaged {
    private Integer id;
    private String name;
    private String parklotName;
    private String intro;
    private String btName;
}

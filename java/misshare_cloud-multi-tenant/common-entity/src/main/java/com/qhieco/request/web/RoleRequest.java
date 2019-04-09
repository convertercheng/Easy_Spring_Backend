package com.qhieco.request.web;

import lombok.Data;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-8 上午10:18
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class RoleRequest extends QueryPaged {

    String name;
    Long startCreateTime;
    Long endCreateTime;
    Integer level;

}

package com.qhieco.request.web;

import lombok.Data;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-21 下午4:24
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class GatewayRequest extends QueryPaged {

    Integer routerId;
    String parklotName;
    String identifier;
    String name;
    String routerName;
    String routerNumber;
    String type;

}

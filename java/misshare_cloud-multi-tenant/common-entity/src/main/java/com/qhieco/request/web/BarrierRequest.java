package com.qhieco.request.web;

import lombok.Data;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-9 下午3:36
 * <p>
 * 类说明：
 * 道闸查询请求类
 */
@Data
public class BarrierRequest extends QueryPaged {

    private String serverIp;
    private Integer serverPort;
    private String password;
    private String parklotName;
    private String name;

}

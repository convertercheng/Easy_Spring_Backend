package com.qhieco.request.web;

import lombok.Data;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-8 上午10:17
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class QueryPaged {

    Integer sEcho;
    Integer start;
    Integer length;

}

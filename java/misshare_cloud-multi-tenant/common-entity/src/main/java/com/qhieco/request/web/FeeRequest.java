package com.qhieco.request.web;

import lombok.Data;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-28 下午2:55
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class FeeRequest extends QueryPaged {
    private String name;
    private Integer tenantId;
}

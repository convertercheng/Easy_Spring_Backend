package com.qhieco.request.web;

import com.qhieco.response.data.web.AbstractPaged;
import lombok.Data;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 19-1-10 上午9:51
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class TenantRequest extends QueryPaged{

    private String name;
}

package com.qhieco.request.web;

import lombok.Data;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 19-1-11 上午10:05
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class UserWebRequest extends QueryPaged {

    private Integer parentId;
}

package com.qhieco.request.web;

import com.qhieco.response.data.web.AbstractPaged;
import lombok.Data;

/**
 * @author 许家钰 xujiayu0837@163.com
 * @version 2.0.1 创建时间: 18/3/14 下午3:44
 *          <p>
 *          类说明：
 */
@Data
public class AreaRequest extends QueryPaged {
    private Integer parentId;
}

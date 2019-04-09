package com.qhieco.request.api;

import com.qhieco.request.web.QueryPaged;
import lombok.Data;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/7/11 9:52
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class DiscountPackageRequest extends AbstractRequest{
    private Integer parklotId;   // 小区ID
    private Integer mobileUserId;//用户ID
}

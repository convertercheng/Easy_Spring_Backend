package com.qhieco.request.web;

import lombok.Data;

import java.util.List;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/28 上午8:44
 * <p>
 * 类说明：
 *     绑定费用规则
 */
@Data
public class BindFeeRuleRequest {

    private Integer feeRuleId;

    private Integer parklotId;

    private Integer type;

}

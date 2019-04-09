package com.qhieco.request.web;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/27 下午3:45
 * <p>
 * 类说明：
 *     添加预约费用请求类
 */
@Data
public class FeeRuleAddReserveRequest {

    @NotNull
    private String name;

    private String advanceTimeList;

    private String feeList;

}

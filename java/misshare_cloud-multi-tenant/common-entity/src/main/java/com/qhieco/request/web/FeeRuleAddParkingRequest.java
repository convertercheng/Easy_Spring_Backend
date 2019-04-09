package com.qhieco.request.web;

import com.qhieco.commonentity.FeeRuleParking;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/6/25 下午3:45
 * <p>
 * 类说明：
 *     添加预约费用请求类
 */
@Data
public class FeeRuleAddParkingRequest {

    private Integer id;

    private String name;

    private Integer type;

    private List<FeeRuleParking> feeRuleParkings;



}

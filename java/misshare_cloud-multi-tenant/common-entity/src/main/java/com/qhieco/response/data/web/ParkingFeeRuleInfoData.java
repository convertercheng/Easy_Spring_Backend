package com.qhieco.response.data.web;

import com.qhieco.TenantSupport;
import com.qhieco.commonentity.FeeRuleParking;
import lombok.Data;

import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/4/14 11:34
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class ParkingFeeRuleInfoData implements TenantSupport{
    private Integer id;
    private String name;
    private Integer type;
    private Integer tenantId;
    private String tenantName;
    private List<FeeRuleParking> feeRuleParkings;

}

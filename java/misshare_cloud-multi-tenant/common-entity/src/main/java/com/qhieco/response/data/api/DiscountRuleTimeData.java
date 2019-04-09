package com.qhieco.response.data.api;

import com.qhieco.constant.Status;
import lombok.Data;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/7/1 10:46
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class DiscountRuleTimeData {

    /**
     * 基本信息
     */
    private Integer type;
    private Long beginTime;
    private Long endTime;
    private Integer state;
    private String timeStr;

    public String getRuleTimeTypeStr() {
        return Status.RuleTimeTypeStr.find(this.type);
    }
}

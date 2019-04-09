package com.qhieco.response.data.web;

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
    private Integer id;
    private String discountPackageId;
    private Integer type;
    private String beginTime;
    private String endTime;
    private Integer state;
    private Long update_time;
    private Long createTime;

    /**
     * 套餐规格信息
     */
    private Integer sumId;
    private Integer daytime;
    private Integer sumNumber;

    public String getRuleTimeTypeStr() {
        return Status.RuleTimeTypeStr.find(this.type);
    }
}

package com.qhieco.response.data.api;

import com.qhieco.constant.Status;
import com.qhieco.response.data.api.DiscountRuleTimeData;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/7/1 10:46
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class DiscountPackageData {

    /**
     * 基本信息
     */
    private Integer id;
    private String packageName;
    private Integer plateId;
    private String plateNo;
    private Integer state;
    private Integer parklotId;
    private Integer toplimit;
    /**
     * 时段
     */
    private List<DiscountRuleTimeData> ruleTimeList;

    private List<PackageFormatSumData> packageFormatSumDataList;

    private String parkLotNameList;
}

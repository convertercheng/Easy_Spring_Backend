package com.qhieco.response.data.api;

import lombok.Data;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/7/20 13:34
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class DiscountPackageListData {
    private Integer packageId;
    private Integer state;
    private Long endTime;
    private String plateNo;

    private List<ParklotInfo> parklotInfos;

    private List<TimeRule> timeRules;

    @Data
    public static class ParklotInfo {
        Integer parklotId;
        String parklotName;
    }

    @Data
    public static class TimeRule {
        private Integer type;
        private String beginTime;
        private String endTime;
    }
}

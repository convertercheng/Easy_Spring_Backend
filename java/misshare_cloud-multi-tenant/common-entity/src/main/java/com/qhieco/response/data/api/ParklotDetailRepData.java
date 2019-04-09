package com.qhieco.response.data.api;

import com.qhieco.commonentity.File;

import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/27 10:20
 * <p>
 * 类说明：
 *   车场详情响应参数
 */
@lombok.Data
public class ParklotDetailRepData {

    private  List<File> files;

    private List<ParklocShareRepData> parklocShareRepData;

    private Integer parklotId;

    private String parklotName;

    private Integer parklotKind;

    private String parklotAddress;

    private Integer totalAmount;

    private Integer idleAmount;

    /**
     * 当前时间可预约的车位数
     */
    private Integer reservableAmount;

    private String distance;

    private Integer parklotType;

    private Double parklotLng;

    private Double parklotLat;

    private Integer leftAmount;

    private Integer leftAmountType;

    private Integer packageState;


}

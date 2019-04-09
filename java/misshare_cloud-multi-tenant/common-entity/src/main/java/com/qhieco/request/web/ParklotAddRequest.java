package com.qhieco.request.web;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/26 下午2:33
 * <p>
 * 类说明：
 *     添加停车区
 */
@Data
public class ParklotAddRequest {

    private Integer areaId;

    private String name;

    private String address;

    private Integer type;

    private String latLng;

    private String navLatLng;

    private Integer totalAmount;

    private String contactName;

    private String contactPhone;

    private Integer signedAmount;

    private Integer kind;

    private Integer leftAmount;

    private Integer leftAmountType;

    private Integer state;

    private String feeIntro;

    private List<MultipartFile> files;

    private List<Integer> fileIds;

    private Integer reserveFeeRuleId;

    private Integer parkingFeeRuleId;

    private Integer minPublishInterval;

    private Integer minSharePeriod;

    private Integer freeCancellationTime;

    private Integer maxDelayTime;

    private Integer minChargePeriod;

    private Integer advanceChangeTime;

    private Integer advanceReservationTime;

    private Integer platformPercent;

    private Integer ownerPercent;

    private Integer propCompPercent;

    private Integer operatorPercent;

    private String adminName;

    private String lastAdminPhone;

    private String adminPhone;

    private Integer parklotId;

    public ParklotAddRequest() {
    }

    public ParklotAddRequest(Integer areaId, String name, String address, Integer type, String latLng, String navLatLng, Integer totalAmount, String contactName, String contactPhone, Integer signedAmount, Integer kind, Integer leftAmount, Integer leftAmountType, Integer state, String feeIntro, List<MultipartFile> files, List<Integer> fileIds, Integer reserveFeeRuleId, Integer parkingFeeRuleId, Integer minPublishInterval, Integer minSharePeriod, Integer freeCancellationTime, Integer maxDelayTime, Integer minChargePeriod, Integer advanceChangeTime, Integer advanceReservationTime, Integer platformPercent, Integer ownerPercent, Integer propCompPercent, Integer operatorPercent, String adminName, String adminPhone, Integer parklotId) {
        this.areaId = areaId;
        this.name = name;
        this.address = address;
        this.type = type;
        this.latLng = latLng;
        this.navLatLng = navLatLng;
        this.totalAmount = totalAmount;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.signedAmount = signedAmount;
        this.kind = kind;
        this.leftAmount = leftAmount;
        this.leftAmountType = leftAmountType;
        this.state = state;
        this.feeIntro = feeIntro;
        this.files = files;
        this.fileIds = fileIds;
        this.reserveFeeRuleId = reserveFeeRuleId;
        this.parkingFeeRuleId = parkingFeeRuleId;
        this.minPublishInterval = minPublishInterval;
        this.minSharePeriod = minSharePeriod;
        this.freeCancellationTime = freeCancellationTime;
        this.maxDelayTime = maxDelayTime;
        this.minChargePeriod = minChargePeriod;
        this.advanceChangeTime = advanceChangeTime;
        this.advanceReservationTime = advanceReservationTime;
        this.platformPercent = platformPercent;
        this.ownerPercent = ownerPercent;
        this.propCompPercent = propCompPercent;
        this.operatorPercent = operatorPercent;
        this.adminName = adminName;
        this.adminPhone = adminPhone;
        this.parklotId = parklotId;
    }
}

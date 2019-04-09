package com.qhieco.request.web;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/5/28 下午5:30
 * <p>
 * 类说明：
 *
 */
@Setter
@Getter
public class ParklotInfoRequest {

    private Integer areaId;

    private String address;

    private String name;

    private Integer totalAmount;

    private String contactName;

    private String contactPhone;

    private Integer signedAmount;

    private Integer leftAmount;

    private Integer isShow;

    private String latLng;

    private String naviLatLng;

    private Integer type;

    private Integer kind;

    private Integer state;

    private String adminName;

    private String adminPhone;

    private String lastAdminName;

    private String lastAdminPhone;

    private String feeIntro;

    private Integer parklotId;

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

    private Integer platformAppointmentPercent;

    private Integer propCompAppointmentPercent;

    private List<MultipartFile> files;

    private List<Integer> fileIds;

    private Integer chargeType;

    private Integer packageId;//套餐ID
    private Integer packageState = 1;

    @Override
    public String toString() {
        return "ParklotInfoRequest{" +
                "areaId=" + areaId +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", totalAmount=" + totalAmount +
                ", contactName='" + contactName + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", signedAmount=" + signedAmount +
                ", leftAmount=" + leftAmount +
                ", isShow=" + isShow +
                ", latLng='" + latLng + '\'' +
                ", naviLatLng='" + naviLatLng + '\'' +
                ", type=" + type +
                ", kind=" + kind +
                ", state=" + state +
                ", adminName='" + adminName + '\'' +
                ", adminPhone='" + adminPhone + '\'' +
                ", lastAdminName='" + lastAdminName + '\'' +
                ", lastAdminPhone='" + lastAdminPhone + '\'' +
                ", feeIntro='" + feeIntro + '\'' +
                ", parklotId=" + parklotId +
                ", reserveFeeRuleId=" + reserveFeeRuleId +
                ", parkingFeeRuleId=" + parkingFeeRuleId +
                ", minPublishInterval=" + minPublishInterval +
                ", minSharePeriod=" + minSharePeriod +
                ", freeCancellationTime=" + freeCancellationTime +
                ", maxDelayTime=" + maxDelayTime +
                ", minChargePeriod=" + minChargePeriod +
                ", advanceChangeTime=" + advanceChangeTime +
                ", advanceReservationTime=" + advanceReservationTime +
                ", platformPercent=" + platformPercent +
                ", ownerPercent=" + ownerPercent +
                ", packageId=" + packageId +
                ", propCompPercent=" + propCompPercent +
                ", operatorPercent=" + operatorPercent +
                ", files=" + files +
                ", fileIds=" + fileIds +
                '}';
    }
}

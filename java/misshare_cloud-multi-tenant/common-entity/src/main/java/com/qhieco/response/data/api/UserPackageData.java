package com.qhieco.response.data.api;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
public class UserPackageData {
    private Integer id;
    private Integer mobileUserId;
    private Integer packageId;
    private Integer plateId;
    private Integer parklotId;
    private Long realStartTime;

    private Long realEndTime;
    private Integer state;
    private Long createTime;
}

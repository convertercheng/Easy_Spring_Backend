package com.qhieco.request.api;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/26 10:46
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class WxBindRequest extends AbstractRequest{
    private String plateNumber;
    private String phone;
    private Long startTime;
    private Long endTime;
    private Long shareStartTime;
    private Long shareEndTime;
    private BigDecimal totalFee;
    private Integer parklotId;
    private Integer parklocId;
    private String ip;
    private String phoneModel;
    private String unionId;
    private String openId;
    private BigDecimal realFee;
    private Integer userId;
    private String jpushId;
    private String appId;
    private String code;
    private String secret;
    private String encryptedData;
    private String iv;
    private String sessionKey;
    private String macId;
    private Integer parklotDistrictId;
}

package com.qhieco.response.data.api;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @author  xujiayu
 * Created by xujiayu on 17/9/29.
 */
@Data
public class LockData{
    private String type;
    private String mac;
    private String payload;
    @SerializedName("gateway_id")
    private String gatewayId;
    private DeviceData device;
    private Integer position;
    private Double battery;

}

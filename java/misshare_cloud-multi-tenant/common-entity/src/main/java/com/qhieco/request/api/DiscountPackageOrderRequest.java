package com.qhieco.request.api;

import lombok.Data;

@Data
public class DiscountPackageOrderRequest extends AbstractRequest{
    private Integer parklotId;
    private Integer plateId;
    private Integer packageId;
    private Integer packageFormatSumId;
    private Integer mobileUserId;
}

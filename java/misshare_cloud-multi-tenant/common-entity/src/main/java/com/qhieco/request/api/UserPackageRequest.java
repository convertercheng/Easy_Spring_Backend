package com.qhieco.request.api;

import lombok.Data;

@Data
public class UserPackageRequest extends AbstractRequest{
    private Integer plateId;
    private Integer state;
    private Integer packageId;
    private Integer parklotId;
}

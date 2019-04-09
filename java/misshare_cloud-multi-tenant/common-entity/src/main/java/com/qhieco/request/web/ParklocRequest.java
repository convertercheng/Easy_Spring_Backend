package com.qhieco.request.web;

import lombok.Data;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-27 上午11:04
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class ParklocRequest extends QueryPaged {
    private String parklotName;
    private String number;
    private String lockSerialNumber;
    private String userName;
    private String userPhone;
}

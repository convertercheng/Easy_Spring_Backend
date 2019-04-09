package com.qhieco.response.data.web;

import lombok.Data;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/4/6 下午7:48
 * <p>
 * 类说明：
 *     编辑车位响应
 */
@Data
public class ParklocEditData {

    private String parklotName;

    private Integer parklotId;

    private String userName;

    private Integer userId;

    private String phone;

    private String number;

    private String macName;

    private Integer lockId;
    private Integer parklotDistrictId;
    private String parklotDistrictName;

}

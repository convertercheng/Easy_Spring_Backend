package com.qhieco.request.web;

import lombok.Data;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/29 下午6:09
 * <p>
 * 类说明：
 *     web端新增车位请求接口
 */
@Data
public class ParklocAddRequest {

    private Integer parklocId;

    private Integer parklotId;

    private Integer userId;

    private String number;

    private Integer lockId;

    private Integer recommend;

    private Integer parklotDistrictId;

}

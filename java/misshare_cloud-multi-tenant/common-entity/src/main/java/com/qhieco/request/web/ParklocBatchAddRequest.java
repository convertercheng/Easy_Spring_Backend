package com.qhieco.request.web;

import lombok.Data;

import java.util.List;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/4/6 下午5:11
 * <p>
 * 类说明：
 *     web端批量添加车位接口
 */
@Data
public class ParklocBatchAddRequest {

    private Integer parklotId;

    private Integer userId;

    private List<String> numbers;

    private Integer parklotDistrictId;

}

package com.qhieco.trafficmanage.entity.response;

import lombok.Data;

/**
 * @author 郑旭 790573267@qq.com
 * @version 2.0.1 创建时间: 18-6-13  下午1:32
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class DelayEnterResponse extends BaseResponse {

    //服务商入场系统记录流水号
    private String RCCSPTLS;
    //返回结果
    private String FHJG;
}

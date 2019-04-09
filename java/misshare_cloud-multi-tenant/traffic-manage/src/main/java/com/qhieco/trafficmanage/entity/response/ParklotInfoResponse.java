package com.qhieco.trafficmanage.entity.response;

import lombok.Data;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-6-12 上午10:45
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class ParklotInfoResponse extends BaseResponse {
    //服务商系统记录流水号
    private String CSPTLS;
    //处理结果
    private String CLJG;
    //处理结果描述
    private String CLJGMS;
}

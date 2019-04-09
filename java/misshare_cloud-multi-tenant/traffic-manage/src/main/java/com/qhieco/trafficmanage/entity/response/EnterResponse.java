package com.qhieco.trafficmanage.entity.response;

import lombok.Data;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-6-12 上午10:46
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class EnterResponse extends BaseResponse {
    //返回延迟标志
    private String FHYCBZ;
    //车牌号码
    private String CPHM;
    //处理结果
    private String RCCLJG;
    //处理结果描述
    private String RCCLJGMS;
    //是否支持代扣
    private String DKZC;
}

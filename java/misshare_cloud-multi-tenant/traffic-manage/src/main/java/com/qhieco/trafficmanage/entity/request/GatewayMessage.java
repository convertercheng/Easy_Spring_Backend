package com.qhieco.trafficmanage.entity.request;

import lombok.Data;

/**
 * @author 郑旭 790573267@qq.com
 * @version 2.0.1 创建时间: 18-6-13  下午2:30
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class GatewayMessage {
    // 进出口编号
    private String JCKBH;
    // 进出口名称
    private String JCKMC;
    //第三方地图接口 0-百度;1-高德;2-其它
    private String JCKJWDBZ;

    // 进出口经度
    private String JCKJD;
    // 进出口纬度
    private String JCKWD;
}

package com.qhieco.trafficmanage.entity.request;

import lombok.Data;


/**
 * @author 郑旭 790573267@qq.com
 * @version 2.0.1 创建时间: 18-6-21  上午10:27
 * <p>
 * 类说明： 车辆进出场请求类
 * ${description}
 */
@Data
public class CarAccessUploadRequest {

    //上传的图片文件路径
    private String sceneImage;

    //时间戳
    private long timestamp;

    //监测点
    private String monitoringPoints;

    //车道
    private String Lane;

    //第几幅图片
    private String imageIndex;

    //位置区域信息
    private String regionInfo;
}

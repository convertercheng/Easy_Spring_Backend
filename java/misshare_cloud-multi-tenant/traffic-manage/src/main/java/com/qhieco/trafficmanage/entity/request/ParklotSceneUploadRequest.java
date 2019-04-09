package com.qhieco.trafficmanage.entity.request;

import lombok.Data;

/**
 * @author 郑旭 790573267@qq.com
 * @version 2.0.1 创建时间: 18-6-21  下午3:38
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class ParklotSceneUploadRequest {

    //上传的图片文件路径
    private String sceneImage;
    //停车场ID
    private String TCCID;
}

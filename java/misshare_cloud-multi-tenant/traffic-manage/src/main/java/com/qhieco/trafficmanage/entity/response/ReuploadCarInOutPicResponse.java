package com.qhieco.trafficmanage.entity.response;

import lombok.Data;

/**
 * @author 郑旭 790573267@qq.com
 * @version 2.0.1 创建时间: 18-6-22  上午9:45
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class ReuploadCarInOutPicResponse extends BaseResponse{
    //出场处理结果
    private String CLJG;
    //处理结果描述
    //0000-成功;4002-出场消息内容有误
    private String CLJGMS;
}

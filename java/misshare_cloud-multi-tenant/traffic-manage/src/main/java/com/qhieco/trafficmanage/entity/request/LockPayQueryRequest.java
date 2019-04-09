package com.qhieco.trafficmanage.entity.request;

import lombok.Data;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-6-12 上午11:08
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class LockPayQueryRequest extends BaseRequest{

    //停车场管理系统中的用户
    private String PTID;
    //手机号
    private String SJH;
    //车牌号码
    private String CPHM;

}

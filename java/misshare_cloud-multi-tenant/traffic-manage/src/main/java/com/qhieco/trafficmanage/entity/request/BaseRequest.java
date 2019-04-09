package com.qhieco.trafficmanage.entity.request;

import lombok.Data;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-6-12 上午10:10
 * <p>
 * 类说明：
 * 基础请求信息，由请求服务自动生成，无需填充
 */
@Data
public class BaseRequest {
    //服务商在平台的注册编号
    private String CSID;
    //报文版本
    private String MSGVER;
    //服务商系统记录流水号
    private String CSPTLS;
    //psnmngreply
    private String JYLX;
    //请求时间
    private String QQSJ;
}

package com.qhieco.trafficmanage.entity.request;

import lombok.Data;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-6-12 上午10:54
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class CancelSignRequest extends BaseRequest {
    //平台解约流水
    private String PTJYLS;
    //停车场管理系统中的用户
    private String PTID;
    //手机号
    private String SJH;
    //代扣方式
    private String DKPT;
    //代扣方商户
    private String DKPTPID;
    //代扣方用户
    private String DKPTUID;
    //解约时间
    private String JYSJ;
    //解约流水
    private String JYLS;
}

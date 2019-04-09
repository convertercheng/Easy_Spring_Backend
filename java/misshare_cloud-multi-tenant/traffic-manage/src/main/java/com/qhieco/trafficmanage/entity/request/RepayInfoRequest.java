package com.qhieco.trafficmanage.entity.request;

import lombok.Data;

import java.util.List;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-6-12 上午11:07
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class RepayInfoRequest extends BaseRequest {
    //停车场管理系统的用户ID
    //小猫就是小猫的 userid,蜜蜂就是蜜蜂的 userid
    //如果非服务商本系统的用户,本域不填
    private String PTID;

    //服务商出场系统记录流水号
    //对应实际出场的那条记录对应的流水
    private String CCCSPTLS;

    //补扣付款时间
    //yyyy-MM-dd HH:mm:ss
    private String BKFKSJ;

    //付款金额
    //0-成功
    private String FKJE;

    //付款结果
    //如果 FKJG 为 0,则本项必填
    private String FKJG;

    //付款信息
    //如果 FKJG 为 0,则本项必填
    private List<PaymentMessage> FKXX;
}

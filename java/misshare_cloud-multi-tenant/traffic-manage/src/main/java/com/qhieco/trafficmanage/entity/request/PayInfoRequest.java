package com.qhieco.trafficmanage.entity.request;

import lombok.Data;

import java.util.List;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-6-12 上午10:49
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class PayInfoRequest extends BaseRequest{
    //停车场管理系统的用户ID
    //如:小猫就是小猫的 userid,蜜蜂就是蜜蜂的 userid
    //如果非服务商本系统的用户,本域不填
    private String PTID;
    //服务商出场系统记录流水号
    //对应实际出场的那条记录对应的流水
    private String CCCSPTLS;
    //付款时间
    //yyyy-MM-dd HH:mm:ss
    private String FKSJ;
    //付款金额
    //如果 FKJG 为 0,则本项必填
    private String FKJE;
    //付款结果
    //0-成功;1-代扣失败
    private String FKJG;
    //代扣失败渠道
    //如果 FKJG 为 1,则本项必填
    //0-支付宝;1-微信
    private String DKSBQD;
    //付款信息
    //如果 FKJG 为 0,则本项必填
    //Json 内容为下表所示,以 jsonlist转 string 成为该属性的值
    private List<PaymentMessage> FKXX;
}

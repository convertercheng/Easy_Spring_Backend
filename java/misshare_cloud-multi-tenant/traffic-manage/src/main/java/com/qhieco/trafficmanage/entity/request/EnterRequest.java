package com.qhieco.trafficmanage.entity.request;

import lombok.Data;

import java.util.List;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-6-12 上午10:46
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class EnterRequest extends BaseRequest {
    // 停车场 ID
    private String TCCID ;
    // 停车场名称
    private String TCCMC ;
    // 停车场进口 ID
    private String TCCJKID ;
    // 进出口编号
    private String JCKBH ;
    // 进出口名称
    private String JCKMC ;
    // 车牌号码
    private String CPHM ;
    //车牌类型
    private String CPLX;
    // 通过时间
    private String TGSJ ;
    //置信度
    private String ZXD ;
    //图片存放工控机IP
    private String GKJIP ;
    //机动车辆进场图片的全名列表
    private List<String> TPIDS;
    //机动车辆进场图片是否由服务商主动上传,0-否，1-是；为 0 是 TPIDS 为图片可访问路径
    private String TPCSCC ;
    //车标 车辆的中文标志名称
    private String CB ;
    //车辆颜色
    private String CSYS ;
    // 是否完成识别 0 为未完成,1 为已完成
    private String SFWC ;
    // 补传标志 0 补传数据,1 实时数据
    private String BCBZ ;
    // 停车场注册总车位数
    private String TCCCWS ;
    // 在场车辆数
    private String ZCCLS ;
    // 剩余车位数
    private String SYCWS ;
    // 月卡标志 0 为非月卡,1 为月卡
    private String YKBZ ;
    //手机号 如果已签约代扣,上传
    private String SJH ;
    // 代扣方式,如果已签约代扣，上传，0-支付宝；1-微信；98-其它；99-无需信用代扣。如果签约多渠道，只能上传一种，上传哪种建议服务商提供用户自行选择代扣顺序
    private String DKPT ;
    // http 回调地址 异步通知的数据会使用该地址返回对应需要的数据
    private String HTTPHDDZ ;
}

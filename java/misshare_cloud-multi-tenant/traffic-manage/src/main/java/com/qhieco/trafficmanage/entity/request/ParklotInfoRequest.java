package com.qhieco.trafficmanage.entity.request;

import lombok.Data;

import java.util.List;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-6-12 上午10:15
 * <p>
 * 类说明：
 * 停车场信息
 */
@Data
public class ParklotInfoRequest extends BaseRequest{
    //停车场 ID 在申请接入时由平台管理方统一编制发放,平台内唯一
    private String TCCID;
    //停车场名称 停车场申办经营性许可证所使用详细准确名称
    private String TCCMC;
    //经纬度地图标准 0-百度;1-高德;2-其它
    private String JWDBZ;
    //停车场经度 停车场中心点或主出入口经度
    private String TCCJD;
    //停车场纬度 停车场中心点或主出入口纬度
    private String TCCWD;
    //停车场注册总车位数 停车场申办经营性许可证申报核准总车位数
    private String TCCCWS;
    //可容纳总车位数 指停车场满负荷时实际可提供的总车位数
    private String KRNCWS;
    //服务商停车场管理系统内更新本条记录的时间
    private String GXSJ;
    //停车场详细地址 停车场申办经营性许可证所使用详细准确地址
    private String XXDZ;
    //经营性停车场许可证号
    private String XKZH;
    //停车场图片的全名列表
    private List<String> TPIDS;
    //停车场图片是否由服务商主动上传
    private String TPCSCC;
    //进出口详细信息
    private List<GatewayMessage> JCKXX;
}

package com.qhieco.trafficmanage.entity.request;

import lombok.Data;

import java.util.List;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-6-12 上午10:47
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class LeaveRequest extends BaseRequest{
    // 停车场 ID
    private String TCCID;
    // 停车场名称 停车场申办经营性许可证所使用详细准确名称
    private String TCCMC;
    // 停车场出口 ID
    private String TCCCKID;
    // 进出口编号 编号从 1 开始
    private String JCKBH;
    // 进出口名称
    private String JCKMC;
    // 车牌号码
    private String CPHM;
    // 车牌类型
    private String CPLX;
    // 通过时间
    private String TGSJ;
    // 置信度
    private String ZXD;
    // 图片存放工控机IP
    private String GKJIP;
    //机动车辆出场图片的全名列表
    private List<String> TPIDS;
    // 机动车辆出场图片是否由服务商主动上传
    private String TPCSCC;
    // 车标 车辆的中文标志名称,例如:本田
    private String CB;
    // 车辆颜色
    private String CSYS;
    // 是否完成识别 0 为未完成,1 为已完成
    private String SFWC;
    // 补传标志 0 为补传数据,1 为实时数据
    private String BCBZ;
    // 停车场注册总车位数
    private String TCCCWS;
    // 在场车辆数
    private String ZCCLS;
    // 剩余车位数
    private String SYCWS;
    // 入场时间
    private String RCSJ;
    // 停车时长 以秒为单位
    private String TCSC;
    // 月卡标志 0 为非月卡,1 为月卡
    private String YKBZ;
}

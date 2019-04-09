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
public class PhotoRequest extends BaseRequest{
    //进出场标志 0-进场;1-出场
    private String JCCBZ;
    //进出场的服务商系统记录流水号
    private String JCCCSPTLS;
    //通过时间 24 小时制
    private String TGSJ;
    //机动车辆进出场图片的全名列表
    private List<String> TPIDS;
    //机动车辆进出场图片是否由服务商主动上传 0-否,1-是;为 0 是 TPIDS 为图片可访问路径
    private String TPCSCC;
}

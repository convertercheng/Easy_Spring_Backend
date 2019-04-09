package com.qhieco.trafficmanage.entity.request;

import lombok.Data;

import java.util.List;

/**
 * @author 郑旭 790573267@qq.com
 * @version 2.0.1 创建时间: 18-6-22  上午9:44
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class ReuploadCarInOutPicRequest extends BaseRequest{
    //进出场标志 0-进场;1-出场
    private String JCCBZ;
    //进出场的服务商系统记录流水号 单服务商停车场管理系统内唯一,平台需要根据这个流水去找原记录
    private String JCCCSPTLS;
    //通过时间 24 小时制,格式如例:013-02-23 22:11:30,日期与时间必须有一个空格
    private String TGSJ;
    //机动车辆进出场图片的全名列表 [name1.jpg,name2.jpg],以jsonlist 转 string
    private List<String> TPIDS;
    //机动车辆进出场图片是否由服务商主动上传 0-否,1-是;为 0 是 TPIDS 为图片可访问路径
    private String TPCSCC;

}

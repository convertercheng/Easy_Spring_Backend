package com.qhieco.trafficmanage.entity.request;

import lombok.Data;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-6-12 上午11:06
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class DelayEnterRequest extends BaseRequest{
    //服务商入场系统记录流水号
    private String RCCSPTLS;
    //车牌号码
    private String CPHM;
    //处理结果
    private String RCCLJG;
    //处理结果描述
    private String RCCLJGMS;
    //代扣可信度 Y-可信; N-不可信,当 DKKXD 为 N 时,XJPTBZ、DKPTBZ、DKSBBZ 会出现相关提示
    private String DKKXD;
    //是否星级用户 Y-是;N-否
    private String XJPTBZ;
    //是否允许代扣 Y-是;N-否
    private String DKPTBZ;
    //代扣是否被锁定 Y-是;N-否
    private String DKSDBZ;
}

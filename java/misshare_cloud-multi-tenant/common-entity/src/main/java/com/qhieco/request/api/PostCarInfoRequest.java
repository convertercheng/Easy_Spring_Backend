package com.qhieco.request.api;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 刘江茳 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/4/22 下午10:11
 * <p>
 * 类说明：
 *     道闸信息请求
 */
@Data
public class PostCarInfoRequest {

    private Integer appId;
    private String key;
    private String plateNo;
    private String enteyTime;
    private Integer parkId;
    private String parkName;
    private String entryPlace;
    private String imgName;
    private String leaveTime;
    private String leavePlace;

}

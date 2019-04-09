package com.qhieco.barrier.keytop.request;

import lombok.Data;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/4/26 11:11
 * <p>
 * 类说明：
 * ${desription}
 */
@Data
public class KeyTopCancelRequest {



    private Integer appId;
    private String key;
    private Integer parkId;
    private String orderNo;

    public KeyTopCancelRequest(){}

    public KeyTopCancelRequest(Integer appId, String key, Integer parkId, String plateNo) {
        this.appId = appId;
        this.key = key;
        this.parkId = parkId;
        this.orderNo = orderNo;
    }

}

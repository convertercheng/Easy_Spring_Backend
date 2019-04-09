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
public class KeyTopParkingRequest {



    private Integer appId;
    private String key;
    private Integer parkId;
    private String plateNo;
    private Integer type;
    private String addrId;
    private String enterTime;
    private String leaveTime;
    private String userName;
    private String tel;
    private String failureTime;

    public KeyTopParkingRequest(){}

    public KeyTopParkingRequest(Integer appId, String key, Integer parkId,String plateNo,Integer type,String enterTime,String userName,String tel,String failureTime) {
        this.appId = appId;
        this.key = key;
        this.parkId = parkId;
        this.plateNo = plateNo;
        this.type = type;
        this.enterTime = enterTime;
        this.userName = userName;
        this.tel = tel;
        this.failureTime = failureTime;

    }

}

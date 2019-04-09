package com.qhieco.response.data.api;

import lombok.Data;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/21 18:23
 * <p>
 * 类说明：
 * 车位锁列表返回实体
 */
@Data
public class ParklocLockRespData {
    /**
     * 车位锁id
     */
    private Integer lockId;
    /**
     *车位id
     */
    private Integer parklocId;
    /**
     * 车场id
     */
    private Integer parklotId;
    /**
     * 车场名称
     */
    private String parklotName;
    /**
     * 车位编号
     */
    private String parklocNumber;
    /**
     * 车位锁蓝牙名称
     */
    private String btName;
    /**
     * 车位锁蓝牙密码
     */
    private String btPwd;

    /**
     * 锁类型
     */
    private Integer type;
    /**
     * 车位共享时间段列表
     */
    private List<ShareInfo> shareList;
}

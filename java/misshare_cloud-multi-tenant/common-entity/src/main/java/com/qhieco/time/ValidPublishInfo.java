package com.qhieco.time;

import lombok.Data;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/28 19:51
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class ValidPublishInfo {
    /**
     * publish表id
     */
    private int publishId;
    /**
     * 发布方式
     */
    private int mode;
    /**
     * 车位id
     */
    private int parklocId;
    /**
     * 发布开始时间
     */
    private long startTime;
    /**
     * 发布结束时间
     */
    private long endTime;
}

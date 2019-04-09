package com.qhieco.response.data.api;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/2 17:01
 * <p>
 * 类说明：
 * ${说明}
 */
@lombok.Data
public class AdvertRepData {
    /**
     * 广告页文件地址
     */
    private String filePath;
    /**
     * 倒计时
     */
    private int countdown;
    /**
     * 是否可跳
     */
    private int jumpable;
    /**
     * 跳转地址
     */
    private String href;
}
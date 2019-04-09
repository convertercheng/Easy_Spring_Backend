package com.qhieco.response.data.api;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 15:02
 * <p>
 * 类说明：
 *     个人中心返回数据
 */
@lombok.Data
public class PersonCenterRepData {

    /**
     * 用户类型，0车主，1业主，2管理员
     */
    private Integer userType;
    /**
     * 用户手机号码
     */
    private String phone;
    /**
     * 用户可用卡券数
     */
    private Integer couponCount;
    /**
     * 用户预约的时间段
     */
    private String reservePeriod;
    /**
     * 头像所在路径
     */
    private String avatarPath;
}

package com.qhieco.commonentity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应用户三方信息表
 */
@Data
@Entity
@Table(name = "t_user_extra_info")
public class UserExtraInfo {

    public UserExtraInfo() {
    }

    public UserExtraInfo(Integer mobileUserId, String jpushRegId, String wxOpenId, String wxUnionId, String alipayUserId, String alipayLogonId,String wxBindOpenId) {
        this.mobileUserId = mobileUserId;
        this.jpushRegId = jpushRegId;
        this.wxOpenId = wxOpenId;
        this.wxUnionId = wxUnionId;
        this.wxBindOpenId=wxBindOpenId;
        this.alipayUserId = alipayUserId;
        this.alipayLogonId = alipayLogonId;
    }

    @Id
    @GeneratedValue
    private Integer id;

    /**
     * 用户id
     */
    @Column(name = "mobile_user_id")
    private Integer mobileUserId;

    /**
     * 极光推送id
     */
    @Column(name = "jpush_reg_id", nullable = false)
    private String jpushRegId;

    /**
     * 微信支付openid
     */
    @Column(name = "wx_open_id", nullable = false)
    private String wxOpenId;

    /**
     * 微信绑定openid
     */
    @Column(name = "wx_bind_open_id", nullable = false)
    private String wxBindOpenId;

    /**
     * 微信绑定unionId
     */
    @Column(name = "wx_union_id", nullable = false)
    private String wxUnionId;

    /**
     * 支付宝用户id
     */
    @Column(name = "alipay_user_id", nullable = false)
    private String alipayUserId;

    /**
     * 支付宝昵称，号码或邮箱
     */
    @Column(name = "alipay_logon_id", nullable = false)
    private String alipayLogonId;
}
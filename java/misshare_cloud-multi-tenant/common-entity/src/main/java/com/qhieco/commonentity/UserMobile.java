package com.qhieco.commonentity;

import com.qhieco.constant.Status;
import lombok.Data;

import javax.persistence.*;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应移动用户表
 */
@Data
@Entity
@Table(name = "t_user_mobile")
public class UserMobile {

    public UserMobile() {
    }

    public UserMobile(String name, Integer fileId, String alias, Integer userRankId, String phone, Long registerTime, Long latestLoginTime, Boolean gender, String region, String loginPwd, String bindCardPwd, Byte type, Integer state) {
        this.name = name;
        this.fileId = fileId;
        this.alias = alias;
        this.userRankId = userRankId;
        this.phone = phone;
        this.registerTime = registerTime;
        this.latestLoginTime = latestLoginTime;
        this.gender = gender;
        this.region = region;
        this.loginPwd = loginPwd;
        this.bindCardPwd = bindCardPwd;
        this.type = type;
        this.state = state;
    }

    public static UserMobile autoSignup(String name, String phone){
        UserMobile user =new UserMobile();
        user.name = name;
        user.phone = phone;
        user.registerTime = System.currentTimeMillis();
        user.latestLoginTime = System.currentTimeMillis();
        user.type =  Status.userType.USERTYPE_TWO.getValue().byteValue();
        user.state = Status.Common.VALID.getInt();
        return user;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;

    /**
     * 用户名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 用户头像id
     */
    @Column(name = "file_id")
    private Integer fileId;

    /**
     * 用户昵称
     */
    @Column(name = "alias")
    private String alias;

    /**
     * 用户等级id
     */
    @Column(name = "user_rank_id")
    private Integer userRankId;

    /**
     * 用户手机号码
     */
    @Column(name = "phone")
    private String phone;

    /**
     * 用户注册时间
     */
    @Column(name = "register_time")
    private Long registerTime;

    /**
     * 用户最新登录时间
     */
    @Column(name = "latest_login_time")
    private Long latestLoginTime;

    /**
     * 用户性别
     */
    @Column(name = "gender")
    private Boolean gender;

    /**
     * 用户所在区域
     */
    @Column(name = "region")
    private String region;

    /**
     * 用户身份证号码
     */
    @Column(name = "identity_number")
    private String identityNumber;

    /**
     * 用户登录密码
     */
    @Column(name = "login_pwd")
    private String loginPwd;

    /**
     * 用户绑定卡密码
     */
    @Column(name = "bind_card_pwd")
    private String bindCardPwd;

    /**
     * 用户类型
     */
    @Column(name = "type")
    private Byte type;

    /**
     * 用户积分 默认60分
     */
    @Column(name = "integral")
    private Integer integral;

    /**
     * 状态
     */

    @Column(name = "state")
    private Integer state;

    /**
     * 首次下单
     */
    @Column(name = "is_index_order")
    private Integer isIndexOrder;
}
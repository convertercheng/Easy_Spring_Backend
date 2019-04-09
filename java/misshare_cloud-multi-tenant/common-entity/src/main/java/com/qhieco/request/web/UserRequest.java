package com.qhieco.request.web;

import lombok.Data;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/3/31 9:55
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class UserRequest extends QueryPaged {

    private Integer id;
    private String phone;
    private Integer userType;
    private Integer state;
    private Long beginLatestLoginTime;
    private Long endLatestLoginTime;
    private Long beginRegisterTime;
    private Long endRegisterTime;
    private String name;
    private String identityNumber;
    private String source;
    private String medium;
    private String content;
    private String keyword;
    private String series;

    private String newPass;  // 新密码
    private String checkPass; // 确认新密码
    private Integer userId;
    private Integer pid;    // 所属账户ID
    private String username; // 账户名
    private String password;  // 密码
    private String companyName; // 公司名称
    private String liaisons; // 联系人
    private String liaisonsPhone; // 联系电话
    private Long createTime; // 创建时间

    private String parklots; // 小区id串

    // 套餐
    private Integer isPackage;// 是否存在套餐费车牌号，1是，2否，

}

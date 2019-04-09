package com.qhieco.request.api;

import lombok.Data;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/2/23 下午9:10
 * <p>
 * 类说明：
 *     用户登录或注册接口request实体
 */
@Data
public class UserLoginRequest extends AbstractRequest{


    /**
     * phone : xxxx...xxxx
     * jpush_id : xxxx...xxxx
     * timestamp : xxxxxx
     */

    private String phone;
    private String jpush_id;
    private String identification;
    private String timestamp;
    private String ip;
    private String phoneModel;
    private String openId;
    private String unionId;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getJpush_id() {
        return jpush_id;
    }

    public void setJpush_id(String jpush_id) {
        this.jpush_id = jpush_id;
    }
}

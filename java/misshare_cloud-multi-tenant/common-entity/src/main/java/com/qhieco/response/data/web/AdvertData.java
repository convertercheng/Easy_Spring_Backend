package com.qhieco.response.data.web;

import com.qhieco.constant.Status;
import lombok.Data;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/6 11:53
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class AdvertData {

    private Integer id;

    /**
     * 图片表关联id
     */
    private Integer fileId;

    /**
     * 跳转链接
     */
    private String href;

    /**
     * 倒计时
     */
    private Integer countdown;

    /**
     * 0表示无效，1表示有效
     */
    private Integer state;

    /**
     * 是否可跳
     */
    private Integer jumpable;

    private Long createTime;

    /**
     * 适配的手机类型 0：安卓 1：iOS
     */
    private Integer phoneType;

    /**
     * 文件完整路径
     */
    private String filePath;
    /**
     * 文件名称
     */
    private String name;


    /**
     * desc的字段都是中文描述使用
     */
    private String stateStr;
    private String jumpableStr;
    private String phoneTypeStr;

    public String getStateStr() {
        return Status.Common.find(this.state);
    }

    public String getJumpableStr() {
        return Status.Jumpable.find(this.jumpable);
    }

    public String getPhoneTypeStr() {
        return Status.PhoneType.find(this.phoneType);
    }
}

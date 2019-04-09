package com.qhieco.response.data.api;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 17:01
 * <p>
 * 类说明：
 * 用户最后一次填写发票的信息
 */
@lombok.Data
public class InvoiceLastWriteRepData {
    /**
     * 0是个人 1是企业
     */
    private Integer type;
    /**
     * 发票抬头
     */
    private String title;
    /**
     * 纳税人识别号
     */
    private String taxpayerId;
    /**
     * 电子邮件
     */
    private String email;
}

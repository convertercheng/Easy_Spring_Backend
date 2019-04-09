package com.qhieco.response.data.api;

import lombok.Data;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/14 14:02
 * <p>
 * 类说明：
 * 消息列表回参实体
 */
@Data
public class MessageRespData {
    private Integer messageId;
    /**
     * 推送标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 发送时间
     */
    private Long createTime;
    /**
     * 0个人消息，1活动消息
     */
    private Integer type;
    /**
     * 推送途径，0短信，1极光
     */
    private Integer kind;
    /**
     * 推送跳转链接
     */
    private String href;


    /**
     * 推送图片文件id
     */
    private List<FileRespData> files;
}

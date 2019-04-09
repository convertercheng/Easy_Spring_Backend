package com.qhieco.request.api;

import lombok.Data;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/14 17:29
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class MessageQueryRequest extends  AbstractRequest {
    private Integer user_id;
    /**
     * 消息类型，0个人消息，1活动消息
     */
    private Integer type;
    private Integer page_num;
}

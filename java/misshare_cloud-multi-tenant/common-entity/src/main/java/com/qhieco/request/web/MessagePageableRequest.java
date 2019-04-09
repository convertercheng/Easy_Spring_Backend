package com.qhieco.request.web;

import lombok.Data;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/4/6 9:43
 * <p>
 * 类说明：
 * 消息分页查询类
 */
@Data
public class MessagePageableRequest extends  QueryPaged{
    String phone;
    String content;
    Long startTime;
    Long endTime;

}

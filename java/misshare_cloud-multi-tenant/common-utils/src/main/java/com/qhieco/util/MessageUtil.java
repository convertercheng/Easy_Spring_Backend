package com.qhieco.util;

import java.util.Date;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/27 19:24
 * <p>
 * 类说明：
 * ${description}
 */
public class MessageUtil {
    public static final String MSGTYPE_EVENT = "event";//消息类型--事件
    public static final String MESSAGE_SUBSCIBE = "subscribe";//消息事件类型--订阅事件
    public static final String MESSAGE_UNSUBSCIBE = "unsubscribe";//消息事件类型--取消订阅事件
    public static final String MESSAGE_TEXT = "text";//消息类型--文本消息

    /*
     * 组装文本消息
     */
    public static String textMsg(String toUserName,String fromUserName,String content){
        TextMessage text = new TextMessage();
        text.setFromUserName(fromUserName);
        text.setToUserName(toUserName);
        text.setMsgType(MESSAGE_SUBSCIBE);
        text.setCreateTime(System.currentTimeMillis());
        text.setContent(content);
        return XmlUtil.textMsgToxml(text);
    }

    /*
     * 响应订阅事件--回复文本消息
     */
    public static String subscribeForText(String toUserName,String fromUserName){
        return textMsg(toUserName, fromUserName, "欢迎关注，精彩内容不容错过！！！");
    }

    /*
     * 响应取消订阅事件
     */
    public static String unsubscribe(String toUserName,String fromUserName){
        System.out.println("用户："+ fromUserName +"取消关注~");
        return "";
    }
}

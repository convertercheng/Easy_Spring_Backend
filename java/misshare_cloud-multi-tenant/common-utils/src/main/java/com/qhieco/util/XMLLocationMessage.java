package com.qhieco.util;

import weixin.popular.bean.message.message.Message;
import weixin.popular.bean.message.message.TextMessage;
import weixin.popular.bean.xmlmessage.XMLMessage;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/28 12:46
 * <p>
 * 类说明：
 * ${description}
 */
public class XMLLocationMessage extends XMLMessage {

    private String Title;
    private String Description;
    private String Url;

    public XMLLocationMessage(String toUserName, String fromUserName,String Title,String Description
    ,String Url) {
        super(toUserName, fromUserName, "link");
        this.Title=Title;
        this.Description=Description;
        this.Url=Url;
    }
    @Override
    public String subXML() {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("<Link>");
        stringBuilder.append("<Title>< ![CDATA["+this.Title+"] ]></Title>");
        stringBuilder.append("<Description>< ![CDATA["+this.Description+"] ]></Description>");
        stringBuilder.append("<Url>< ![CDATA["+this.Url+"] ]></Url>");
        stringBuilder.append("</Link>");
        return stringBuilder.toString();
    }

    @Override
    public Message convert() {
       return new LocationMessage(this.toUserName, this.Title,this.Description,this.Url);
    }
}

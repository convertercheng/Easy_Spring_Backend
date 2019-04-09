package com.qhieco.util;

import lombok.Data;
import weixin.popular.bean.message.message.Message;
import weixin.popular.bean.message.message.TextMessage;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/28 13:01
 * <p>
 * 类说明：
 * ${description}
 */
public class LocationMessage extends Message{
    private Text text;

    public LocationMessage(String touser) {
        super(touser, "link");
    }

    public LocationMessage(String touser,String Title,String Description
            ,String Url) {
        this(touser);
        this.text = new Text();
        this.text.setDescription(Description);
        this.text.setUrl(Url);
        this.text.setTitle(Title);
    }

    public Text getText() {
        return this.text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    @Data
    public static class Text {
        private String Title;
        private String Description;
        private String Url;

        public Text() {
        }

    }
}

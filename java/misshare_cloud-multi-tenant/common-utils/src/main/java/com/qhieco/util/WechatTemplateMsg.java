package com.qhieco.util;

import lombok.Data;

import java.util.TreeMap;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/5/15 17:48
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class WechatTemplateMsg {
    private String touser;
    private String template_id;
    private String url;
    private TreeMap<String, TreeMap<String, String>> data;
    /**
     * 参数
     * @param value
     * @param color 可不填
     * @return
     */
    public static TreeMap<String, String> item(String value, String color) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("value", value);
        params.put("color", color);
        return params;
    }
}

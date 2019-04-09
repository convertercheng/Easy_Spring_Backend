package com.qhieco.util;

import lombok.Data;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/27 19:21
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class TextMessage extends  BaseMessage{
    private String Content;
    private String MsgId;
}

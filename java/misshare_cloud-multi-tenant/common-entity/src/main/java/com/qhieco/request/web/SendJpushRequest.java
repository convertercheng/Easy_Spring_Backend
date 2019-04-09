package com.qhieco.request.web;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 18-3-30 上午9:46
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class SendJpushRequest{
    private Integer webUserId;
    private String userIds;
    private String tagIds;
    private Integer type;
    private String title;
    private String content;
    private String href;
    private Integer fileId;

}

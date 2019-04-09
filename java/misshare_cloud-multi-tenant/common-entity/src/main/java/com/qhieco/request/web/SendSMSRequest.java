package com.qhieco.request.web;

import lombok.Data;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 18-3-30 上午9:46
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class SendSMSRequest {

    private Integer webUserId;
    private String userIds;
    private String tagIds;
    private String content;

}

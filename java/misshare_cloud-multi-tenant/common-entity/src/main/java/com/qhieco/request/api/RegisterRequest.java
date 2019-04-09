package com.qhieco.request.api;

import lombok.Data;

/**
 * @author 黄金芽 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/26 10:39
 * <p>
 * 类说明：
 *       设置常用停车场请求类
 */
@Data
public class RegisterRequest extends AbstractRequest {
    private Integer id;
    private String identification;
    private Integer moblieUserId;
    private Integer registerId;

}

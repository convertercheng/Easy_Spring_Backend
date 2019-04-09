package com.qhieco.request.api;

import lombok.Data;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/26 10:39
 * <p>
 * 类说明：
 *       取消发布请求类
 */
@Data
public class PublishCancelRequest extends AbstractRequest {

    String publishIds;

}

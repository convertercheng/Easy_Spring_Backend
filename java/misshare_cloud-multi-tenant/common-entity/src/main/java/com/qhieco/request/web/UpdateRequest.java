package com.qhieco.request.web;

import com.qhieco.request.api.AbstractRequest;
import lombok.Data;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 15:56
 * <p>
 * 类说明：
 * APP版本更新请求参数
 */
@Data
public class UpdateRequest extends AbstractRequest {

    String vcode;

    String updateinfo;

    String forceupdate;

    Integer type;

}

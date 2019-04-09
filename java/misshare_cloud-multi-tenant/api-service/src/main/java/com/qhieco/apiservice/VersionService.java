package com.qhieco.apiservice;

import com.qhieco.request.api.UpdateRequest;
import com.qhieco.response.Resp;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/4/17 17:25
 * <p>
 * 类说明：
 * ${说明}
 */
public interface VersionService {

    Resp updateInfo(UpdateRequest request);
}

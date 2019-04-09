package com.qhieco.webservice;

import com.qhieco.request.web.UpdateRequest;
import com.qhieco.response.Resp;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/6 13:39
 * <p>
 * 类说明：
 *     强制更新的Service
 */
public interface UpdateService {

    /**
     * 保存强制更新
     * @param updateRequest
     * @return
     */
    Resp saveOrUpdate(UpdateRequest updateRequest);
}

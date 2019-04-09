package com.qhieco.apiservice;

import com.qhieco.commonentity.LogOperationMobile;
import com.qhieco.response.Resp;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/17 9:23
 * <p>
 * 类说明：
 * ${description}
 */
public interface SysManageService {

    /**
     * 保存APP用户登录信息
     * @param logOperationMobile
     * @return
     */
    Resp saveUpdateLogApp(LogOperationMobile logOperationMobile);
}

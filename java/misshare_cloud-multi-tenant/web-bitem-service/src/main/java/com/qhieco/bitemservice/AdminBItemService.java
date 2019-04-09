package com.qhieco.bitemservice;


import com.qhieco.response.Resp;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-4-9 下午2:21
 * <p>
 * 类说明：
 * ${description}
 */
public interface AdminBItemService {

    /**
     * 登录函数，负责将管理员信息存入session,更新登录时间
     * @param request servletReq
     * @return resp
     */
    Resp login(HttpServletRequest request);

}

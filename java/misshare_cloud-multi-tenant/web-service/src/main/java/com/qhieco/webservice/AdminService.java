package com.qhieco.webservice;

import com.qhieco.commonentity.UserWeb;
import com.qhieco.request.web.QueryPaged;
import com.qhieco.request.web.UserWebRequest;
import com.qhieco.response.Resp;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-4-9 下午2:21
 * <p>
 * 类说明：
 * ${description}
 */
public interface AdminService {
    /**
     * 登录函数，负责将管理员信息存入session,更新登录时间
     * @param request servletReq
     * @return resp
     */
    Resp login(HttpServletRequest request);

    /**
     * 新增管理员用户
     * @param userWeb userWeb管理员实体
     * @return resp
     */
    Resp save(UserWeb userWeb);

    /**
     * 管理员绑定角色
     * @param userId 管理员id
     * @param roleIds 角色id列表
     * @return resp
     */
    Resp bindRole(Integer userId, Integer[] roleIds);

    /**
     * 管理员解绑角色
     * @param userId 管理员id
     * @param roleIds 角色id列表
     * @return resp
     */
    Resp unbindRole(Integer userId, Integer[] roleIds);

    /**
     * 管理员分页展现
     * @param queryPaged 分页请求类
     * @return resp<List<Admin>>
     */
    Resp all(QueryPaged queryPaged);

    /**
     * 查询管理员拥有的角色
     * @param userId 角色id
     * @return 角色列表
     */
    Resp findRole(Integer userId);

    /**
     * 查询管理员拥有的权限
     * @param userId 角色id
     * @return 权限列表
     */
    Resp findPermission(Integer userId);

    /**
     * @author ashang
     *判断是何种账户，在权限管理中心显示不同页面
     * @param request  request
     * @return 角色判断
     */
    Resp findAccount(HttpServletRequest request);

    /**
     * @author ashang
     * 父账户分页展现
     */
    Resp daddyAll(QueryPaged queryPaged);

    /**
     * @author ashang
     * 子账户分页展现
     */
    Resp sonAll(UserWebRequest queryPaged, HttpServletRequest request);

}

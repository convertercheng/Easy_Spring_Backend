package com.qhieco.webservice;

import com.qhieco.commonentity.Permission;
import com.qhieco.commonentity.UserWeb;
import com.qhieco.request.web.QueryPaged;
import com.qhieco.response.Resp;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-4-10 上午10:42
 * <p>
 * 类说明：
 * ${description}
 */
public interface PermissionService {
    /**
     * 新增权限
     * @param permission　权限entity
     * @return resp
     */
    Resp save(Permission permission);

    /**
     * 权限分页获取
     * @param queryPaged 分页查询类
     * @return resp
     */
    Resp all(QueryPaged queryPaged);

    Boolean check(UserWeb admin, String url);
}

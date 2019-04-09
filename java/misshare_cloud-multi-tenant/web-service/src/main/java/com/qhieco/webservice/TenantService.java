package com.qhieco.webservice;

import com.qhieco.commonentity.Tenant;
import com.qhieco.request.web.TenantRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.AbstractPaged;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 19-1-10 上午9:49
 * <p>
 * 类说明：
 * ${description}
 */
public interface TenantService {

    Resp<AbstractPaged<Tenant>> query(TenantRequest tenantRequest);

    Resp save(Tenant tenant);
}

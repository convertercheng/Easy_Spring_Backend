package com.qhieco.webservice;

import com.qhieco.commonentity.iotdevice.Router;
import com.qhieco.request.web.GatewayRequest;
import com.qhieco.request.web.RouterRequest;
import com.qhieco.response.Resp;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-22 下午3:02
 * <p>
 * 类说明：
 * ${description}
 */
public interface RouterService {
    /**
     * 路由器新增编辑
     * @param router
     * @return
     */
    Resp save(Router router);

    /**
     * 路由器查询
     * @param request
     * @return
     */
    Resp query(RouterRequest request);

    /**
     * 路由器详情
     * @param request
     * @return
     */
    Resp one(GatewayRequest request);

    /**
     * excel导出
     * @param request
     * @param cls
     * @param outputStream
     * @return
     * @throws IOException
     */
    Resp excel(RouterRequest request, Class cls, OutputStream outputStream) throws IOException;

    /**
     * 路由器绑定小区
     * @param routerIds
     * @param parklocId
     * @return
     */
    Resp bind(Integer[] routerIds, Integer parklocId);

    /**
     * 路由器解绑
     * @param routerId
     * @return
     */
    Resp unbind(Integer routerId);

    /**
     * 未绑定路由器查询
     * @param request
     * @return
     */
    Resp unbindQuery(RouterRequest request);
}

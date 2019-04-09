package com.qhieco.webservice;

import com.qhieco.commonentity.iotdevice.Gateway;
import com.qhieco.request.web.GatewayRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.AbstractPaged;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-21 下午4:22
 * <p>
 * 类说明：
 * ${description}
 */
public interface GatewayService {
    /**
     * 网关编辑新增
     * @param gateway
     * @return
     */
    Resp save(Gateway gateway);

    /**
     * 列表查询
     * @param request
     * @return
     */
    Resp<AbstractPaged<Gateway>> query(GatewayRequest request);

    /**
     * 网关详情
     * @param id
     * @return
     */
    Resp one(Integer id);

    /**
     * excel导出
     * @param request
     * @param cls
     * @param outputStream
     * @return
     * @throws IOException
     */
    Resp excel(GatewayRequest request, Class cls, OutputStream outputStream) throws IOException;

    /**
     * 网关绑定车位
     * @param gatewayIds
     * @param parklocId
     * @return
     */
    Resp bind(Integer[] gatewayIds, Integer parklocId);

    /**
     * 网关解绑车位
     * @param gatewayId
     * @return
     */
    Resp unbind(Integer gatewayId);

    /**
     * 未绑定车位网关查询
     * @param request
     * @return
     */
    Resp unbindQuery(GatewayRequest request);
}

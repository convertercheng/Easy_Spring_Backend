package com.qhieco.webservice;

import com.qhieco.commonentity.iotdevice.Access;
import com.qhieco.request.web.AccessRequest;
import com.qhieco.response.Resp;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-15 上午10:33
 * <p>
 * 类说明：
 * ${description}
 */
public interface AccessService {

    /**
     * 存储门禁
     * @param access
     * @return
     */
    Resp save(Access access);

    /**
     * 门禁列表查询
     * @param request
     * @return
     */
    Resp query(AccessRequest request);

    /**
     * 门禁详情查询
     * @param id
     * @return
     */
    Resp one(Integer id);

    /**
     * excel列表导出
     * @param request
     * @param cls
     * @param outputStream
     * @return
     * @throws IOException
     */
    Resp excel(AccessRequest request, Class cls, OutputStream outputStream) throws IOException;

    /**
     * 门禁小区绑定
     * @param accessIds
     * @param parklotId
     * @return
     */
    Resp bind(Integer[] accessIds, Integer parklotId);

    /**
     * 门禁解绑
     * @param accessId
     * @return
     */
    Resp unbind(Integer accessId);

    /**
     * 未绑定门禁查询
     * @param requst
     * @return
     */
    Resp unbindQuery(AccessRequest requst);
}

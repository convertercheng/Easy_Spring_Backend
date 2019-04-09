package com.qhieco.webservice;

import com.qhieco.commonentity.iotdevice.Relaymeter;
import com.qhieco.request.web.RelaymeterRequest;
import com.qhieco.response.Resp;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-20 上午11:07
 * <p>
 * 类说明：
 * ${description}
 */
public interface RelaymeterService {

    /**
     * 新增编辑继电器
     * @param relaymeter
     * @return
     */
    Resp save(Relaymeter relaymeter);

    /**
     * 继电器列表查询
     * @param request
     * @return
     */
    Resp query(RelaymeterRequest request);

    /**
     * 继电器详情
     * @param id
     * @return
     */
    Resp one(Integer id);

    /**
     * ｅｘｃｅｌ导出
     * @param request
     * @param cls
     * @param outputStream
     * @return
     * @throws IOException
     */
    Resp excel(RelaymeterRequest request, Class cls, OutputStream outputStream) throws IOException;

    /**
     * 继电器绑定停车场
     * @param relaymeterIds
     * @param parklotId
     * @return
     */
    Resp bind(Integer[] relaymeterIds, Integer parklotId);

    /**
     * 继电器解绑
     * @param relaymeterId
     * @return
     */
    Resp unbind(Integer relaymeterId);

    /**
     * 未绑定继电器查询
     * @param requst
     * @return
     */
    Resp unbindQuery(RelaymeterRequest requst);

    /**
     * 查询继电器名称是否重复
     * @param name
     * @param state
     * @return
     */
    Relaymeter findRelaymeterByName(String name, Integer state);
    /**
     * 查询继电器名称是否重复
     * @param name
     * @param state
     * @param id
     * @return
     */
    Relaymeter findRelaymeterByName(String name, Integer state, Integer id);

}

package com.qhieco.webservice;

import com.qhieco.commonentity.iotdevice.AbstractIotDevice;
import com.qhieco.request.web.QueryPaged;
import com.qhieco.response.Resp;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-14 下午2:56
 * <p>
 * 类说明：
 * 通用的设备管理业务基础类接口
 */
public interface IotService<T extends AbstractIotDevice, E extends QueryPaged>  {

    /**
     * 新增编辑设备
     * @param data entity对象
     * @return Resp
     */
    Resp save(T data);

    /**
     * 设备列表查询
     * @param request 查询对象，继承自QueryPaged
     * @return Resp
     */
    Resp query(E request);

    /**
     * 解绑设备
     * @param requst 未绑定的设备查询
     * @return
     */
    Resp unbindQuery(E requst);

    /**
     * 设备详情
     * @param id 设备id
     * @return
     */
    Resp one(Integer id);

    /**
     * 设备excel导出
     * @param request 查询对象
     * @param cls　导出对象类
     * @param outputStream　输出文件流
     * @return
     * @throws IOException
     */
    Resp excel(E request, Class cls, OutputStream outputStream) throws IOException;
}

package com.qhieco.webservice;

import com.qhieco.commonentity.iotdevice.Lock;
import com.qhieco.request.web.IdRequest;
import com.qhieco.request.web.LockRequest;
import com.qhieco.response.Resp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-16 下午4:06
 * <p>
 * 类说明：
 * ${description}
 */
public interface LockService {

    /**
     * 车锁新增编辑
     * @param lock
     * @return
     */
    Resp save(Lock lock);

    public Resp saveLock(Lock lock);

    /**
     * 车锁列表查询
     * @param request
     * @return
     */
    Resp query(LockRequest request);

    /**
     * 车锁详情查询
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
    Resp excel(LockRequest request, Class cls, OutputStream outputStream) throws IOException;

    /**
     * 车锁绑定
     * @param lockIds
     * @param parklocId
     * @return
     */
    Resp bind(Integer[] lockIds, Integer parklocId);

    /**
     * 车锁解绑
     * @param lockId
     * @return
     */
    Resp unbind(Integer lockId);

    /**
     * 未绑定车锁查询
     * @param requst
     * @return
     */
    Resp unbindQuery(LockRequest requst);

    /**
     * 车锁日志列表
     * @param request
     * @return
     */
    Resp logPageable(IdRequest request);

    /**
     * 打包下载
     * @param request
     * @param response
     * @param macIds
     * @throws Exception
     */
    void downloadAllQr(HttpServletRequest request, HttpServletResponse response, String macIds)throws Exception;


}

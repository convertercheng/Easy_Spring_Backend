package com.qhieco.webservice;

import com.qhieco.request.web.ApplyParklocRequest;
import com.qhieco.response.Resp;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/29 下午8:04
 * <p>
 * 类说明：
 *     申请工单
 */
public interface ApplyService {

    /**
     * 处理车位申请
     * @param applyParklocRequest 处理申请车位离诶播啊请求
     * @return 处理结果
     */
    Resp processParkloc(ApplyParklocRequest applyParklocRequest);

    /**
     * 分页显示申请工单列表
     * @param applyParklocRequest
     * @return
     */
    Resp query(ApplyParklocRequest applyParklocRequest);

    /**
     * 导出申请工单列表
     * @param applyParklocRequest
     * @param outputStream
     * @param cl
     * @return
     */
    Resp execl(ApplyParklocRequest applyParklocRequest, OutputStream outputStream, Class cl)throws IOException;
}

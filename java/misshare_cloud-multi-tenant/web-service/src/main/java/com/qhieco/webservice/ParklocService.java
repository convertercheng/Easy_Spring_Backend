package com.qhieco.webservice;

import com.qhieco.request.web.*;
import com.qhieco.response.Resp;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-27 上午11:01
 * <p>
 * 类说明：
 * ${description}
 */
public interface ParklocService {
    /**
     * 车位列表查询方法
     * @param request 查询请求
     * @return Resp
     */
    Resp query(ParklocRequest request);

    /**
     * 添加车位
     * @param parklocAddRequest 增加车位请求
     * @return 添加车位
     */
    Resp addParkloc(ParklocAddRequest parklocAddRequest);

    /**
     * 批量添加车位
     * @param parklocBatchAddRequest 批量添加车位请求
     * @return 添加车位结果
     */
    Resp batchAddParkloc(ParklocBatchAddRequest parklocBatchAddRequest);

    /**
     * 编辑车位
     * @param parklocEditRequest 编辑车位请求
     * @return 返回结果
     */
    Resp editParkloc(ParklocEditRequest parklocEditRequest);

    /**
     * 查看车位发布情况
     * @param parklocPublishInfoRequest 车位发布情况请求
     * @return 车位发布情况
     */
    Resp publishInfo(ParklocPublishInfoRequest parklocPublishInfoRequest);

    /**
     * 导出车位列表
     * @param parklocRequest
     * @param outputStream
     * @return
     * @throws IOException
     */
    Resp excel(ParklocRequest parklocRequest, OutputStream outputStream)throws IOException;

}

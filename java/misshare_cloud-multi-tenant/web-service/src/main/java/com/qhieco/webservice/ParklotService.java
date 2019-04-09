package com.qhieco.webservice;

import com.qhieco.request.web.ParklotAddRequest;
import com.qhieco.request.web.ParklotFileRemoveRequest;
import com.qhieco.request.web.ParklotInfoRequest;
import com.qhieco.request.web.ParklotRequest;
import com.qhieco.response.Resp;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/26 下午2:20
 * <p>
 * 类说明：
 *     停车区的Service
 */

public interface ParklotService {

    /**
     * 添加新的停车区
     * @param parklotInfoRequest 添加新停车区的请求
     * @return 返回添加结果
     */
    Resp addNewParklot(ParklotInfoRequest parklotInfoRequest);

    /**
     * 停车区列表查询
     * @param request
     * @return
     */
    Resp query(ParklotRequest request);

    /**
     * 根据区ID查询详情
     * @param id
     * @return
     */
    Resp findOne(Integer id);

    /**
     * 根据停车场id查询管理员id
     * @param id 停车场id
     * @return 管理员id
     */
    Resp getAdmin(Integer id);

    /**
     * 删除文件
     * @param parklotFileRemoveRequest 请求
     * @return 返回结果
     */
    Resp deleteFile(ParklotFileRemoveRequest parklotFileRemoveRequest);

    /**
     * 小区导出
     * @param request 小区查询请求
     * @param outputStream 输出流
     * @return
     * @throws IOException
     */
    Resp excel(ParklotRequest request, OutputStream outputStream) throws IOException;

    /**
     * 打包下载
     * @param request
     * @param response
     * @param parklotIds
     * @throws Exception
     */
    void downloadAllQr(HttpServletRequest request, HttpServletResponse response, String parklotIds)throws Exception;
}

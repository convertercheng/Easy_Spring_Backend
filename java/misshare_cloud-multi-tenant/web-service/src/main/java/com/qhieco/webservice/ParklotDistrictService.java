package com.qhieco.webservice;

import com.qhieco.commonentity.ParklotDistrict;
import com.qhieco.request.web.ParklotDistrictRequest;
import com.qhieco.response.Resp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 18-7-06 上午11:01
 * <p>
 * 类说明：
 * ${description}
 */
public interface ParklotDistrictService {

    /**
     * 分页显示区域列表
     * @param parklotDistrictRequest 请求参数
     * @return Resp
     * @throws Exception 异常
     */
    Resp query(ParklotDistrictRequest parklotDistrictRequest)throws Exception;

    /**
     * 新增或编辑停车场区域信息
     * @param parklotDistrict 车场区域信息
     * @return Resp
     * @throws Exception 异常
     */
    Resp saveUpdate(ParklotDistrict parklotDistrict)throws Exception;

    /**
     * 获取停车场区域详情
     * @param id 主键id
     * @return Resp
     * @throws Exception 异常
     */
    Resp getParklotDistrictInfo(Integer id)throws Exception;

    /**
     * 停车场区域导出
     * @param parklotDistrictRequest,outputStream 请求参数
     * @return Resp
     * @throws Exception 异常
     */
    Resp excel(ParklotDistrictRequest parklotDistrictRequest, OutputStream outputStream)throws Exception;

    /**
     * 打包下载
     * @param request
     * @param response
     * @param macIds
     * @throws Exception
     */
    void downloadAllQr(HttpServletRequest request, HttpServletResponse response, String macIds)throws Exception;

    /**
     * 根据停车场id查询区域列表
     * @param parklotId
     * @return
     */
    Resp getParklotDistrictByParklotList(Integer parklotId)throws Exception;


}

package com.qhieco.webservice;

import com.qhieco.request.web.DiscountPackageRequest;
import com.qhieco.response.Resp;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/7/11 9:44
 * <p>
 * 类说明：
 * ${description}
 */
public interface DiscountPackageService {

    /**
     * 获取套餐详情列表
     * @param request
     * @return
     */
    Resp findDiscountPackageByPage(DiscountPackageRequest request);

    /**
     * 获取套餐统计列表
     * @param request
     * @return
     */
    Resp findPackageStaticByPage(DiscountPackageRequest request);

    /**
     * 获取套餐详细
     * @param request
     * @return
     */
    Resp findPackageDetailed(DiscountPackageRequest request);

    /**
     * 修改套餐状态
     * @param request
     * @return
     */
    Resp updatePackageState(DiscountPackageRequest request);

    /**
     * 修改套餐展示状态
     * @param request
     * @return
     */
    Resp updateParklotState(DiscountPackageRequest request);

    /**
     * 新增/修改套餐详细
     * @param request
     * @return
     */
    Resp saveOrUpdate(DiscountPackageRequest request);

    /**
     * 套餐列表导出
     * @param retuest
     * @param outputStream
     * @param cl
     * @return
     * @throws IOException
     */
    Resp excelPackage(DiscountPackageRequest retuest, OutputStream outputStream, Class cl)throws IOException;

    /**
     * 套餐统计列表导出
     * @param retuest
     * @param outputStream
     * @param cl
     * @return
     * @throws IOException
     */
    Resp excelStaticPackage(DiscountPackageRequest retuest, OutputStream outputStream, Class cl)throws IOException;
}

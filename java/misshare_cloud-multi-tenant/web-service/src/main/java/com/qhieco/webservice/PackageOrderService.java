package com.qhieco.webservice;

import com.qhieco.request.web.PackageOrderRequest;
import com.qhieco.response.Resp;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/7/13 14:47
 * <p>
 * 类说明：
 * ${description}
 */
public interface PackageOrderService {

    /**
     * 获取套餐订单详情列表
     *
     * @param retuest
     * @return
     */
    Resp findPackageOrderByPage(PackageOrderRequest retuest);

    /**
     * 获取套餐订单详细
     * @param retuest
     * @return
     */
    Resp findPackageOrderDetailed(PackageOrderRequest retuest);


    /**
     * 导出套餐订单列表
     * @param retuest outputStream  cl
     * @return
     */
    Resp excelPackageOrder(PackageOrderRequest retuest, OutputStream outputStream, Class cl)throws IOException;

}

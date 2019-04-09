package com.qhieco.webmapper;

import com.qhieco.request.web.PackageOrderRequest;
import com.qhieco.response.data.web.PackageOrderData;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/7/13 14:50
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface PackageOrderMapper {

    /**
     * 分页查询套餐订单信息
     *
     * @param request
     * @return
     */
    List<PackageOrderData> pagePackageOrder(PackageOrderRequest request);
    /**
     * 查询套餐订单信息总记录数
     *
     * @param request
     * @return
     */
    Integer pagePackageOrderTotalCount(PackageOrderRequest request);


    /**
     * 根据ID获取订单详细
     * @param request
     * @return
     */
    PackageOrderData findPackageOrderById(PackageOrderRequest request);

    /**
     * 导出套餐订单列表
     * @param request
     * @return
     */
    List<PackageOrderData> excelPackageOrder(PackageOrderRequest request);
}


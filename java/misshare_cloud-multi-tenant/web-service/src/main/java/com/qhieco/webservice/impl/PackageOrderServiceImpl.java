package com.qhieco.webservice.impl;

import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.web.PackageOrderRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.AbstractPaged;
import com.qhieco.response.data.web.PackageOrderData;
import com.qhieco.util.ExcelUtil;
import com.qhieco.util.RespUtil;
import com.qhieco.webmapper.PackageOrderMapper;
import com.qhieco.webservice.PackageOrderService;
import com.qhieco.webservice.exception.ExcelException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/7/13 14:50
 * <p>
 * 类说明：
 * ${description}
 */
@Service
public class PackageOrderServiceImpl implements PackageOrderService {

    @Autowired
    PackageOrderMapper packageOrderMapper;

    /**
     * 分页查询套餐订单列表
     *
     * @param request
     * @return
     */
    @Override
    public Resp findPackageOrderByPage(PackageOrderRequest request) {
        List<PackageOrderData> list = packageOrderMapper.pagePackageOrder(request);
        Integer count = packageOrderMapper.pagePackageOrderTotalCount(request);
        AbstractPaged<PackageOrderData> data = AbstractPaged.<PackageOrderData>builder().sEcho
                (request.getSEcho() + 1).iTotalRecords(count).
                iTotalDisplayRecords(count).dataList(list).build();
        return RespUtil.successResp(data);
    }

    /**
     * 获取套餐订单详细
     *
     * @param request
     * @return
     */
    @Override
    public Resp findPackageOrderDetailed(PackageOrderRequest request) {
        PackageOrderData packageOrderData = packageOrderMapper.findPackageOrderById(request);
        return RespUtil.successResp(packageOrderData);
    }

    /**
     * 导出套餐订单详细
     *
     * @param request
     * @param outputStream
     * @param cl
     * @return
     * @throws IOException
     */
    @Override
    public Resp excelPackageOrder(PackageOrderRequest request, OutputStream outputStream, Class cl) throws IOException {
        request.setLength(5000);
        request.setStart(0);
        List<PackageOrderData> userDataList = packageOrderMapper.excelPackageOrder(request);
        if (userDataList != null && userDataList.size() == Constants.EMPTY_CAPACITY) {
            throw new ExcelException(Status.WebErr.EMPTY_EXCEL.getCode(), Status.WebErr.EMPTY_EXCEL.getMsg());
        }

        ExcelUtil<PackageOrderData> packageOrderExcelUtil = new ExcelUtil<>(outputStream, PackageOrderData.class);
        packageOrderExcelUtil.write(userDataList);
        return RespUtil.successResp();
    }

}

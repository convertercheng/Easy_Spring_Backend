package com.qhieco.web.controller;

import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.web.PackageOrderRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.PackageOrderData;
import com.qhieco.util.ExcelUtil;
import com.qhieco.util.ParamCheck;
import com.qhieco.webservice.PackageOrderService;
import com.qhieco.webservice.exception.ParamException;
import com.qhieco.webservice.exception.QhieWebException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/7/13 16:20
 * <p>
 * 类说明：套餐订单
 * ${说明}
 */
@RestController
@RequestMapping(value = "/packageorder")
@Slf4j
public class PackageOrderWeb {

    @Autowired
    private PackageOrderService packageOrderService;

    /**
     * 分页获取套餐详情列表
     * @param request
     * @return
     */
    @PostMapping(value = "/pagelist")
    public Resp findPackageOrderByPage(PackageOrderRequest request) {
        Resp checkResp = ParamCheck.check(request, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return packageOrderService.findPackageOrderByPage(request);
    }

    /**
     * 根据套餐ID查询详细
     * @param request
     * @return
     */
    @PostMapping(value = "/findDetailed")
    public Resp findPackageOrderDetailed(PackageOrderRequest request) {
        Resp checkResp = ParamCheck.check(request, "id");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return packageOrderService.findPackageOrderDetailed(request);
    }

    /**
     * 用户列表导出
     * @param response
     * @param request
     */
    @RequestMapping(value = "/excel")
    public void excel(HttpServletResponse response, PackageOrderRequest request){
        if (ExcelUtil.paramCount(request) == Constants.EMPTY_CAPACITY) {
            throw new QhieWebException(Status.WebErr.EMPTY_EXCEL_PARAM);
        }
        // 下载文件的默认名称
        try {
            packageOrderService.excelPackageOrder(request,response.getOutputStream(), PackageOrderData.class);
            response.setHeader("Content-Disposition",
                    "attachment;filename="+ new String(("套餐订单列表" + ".xls").getBytes(), "iso-8859-1"));
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}

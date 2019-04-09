package com.qhieco.web.controller;

import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.web.DiscountPackageRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.DiscountPackageData;
import com.qhieco.response.data.web.DiscountPackageStaticData;
import com.qhieco.util.ExcelUtil;
import com.qhieco.util.ParamCheck;
import com.qhieco.webservice.DiscountPackageService;
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
 * @version 2.0.1 创建时间: 2018/7/11 9:48
 * <p>
 * 类说明：套餐模块
 * ${说明}
 */
@RestController
@RequestMapping(value = "/discountpackage")
@Slf4j
public class DiscountPackageWeb {

    @Autowired
    private DiscountPackageService discountPackageService;

    /**
     * 分页获取套餐详情列表
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/pagelist")
    public Resp findDiscountPackageByPage(DiscountPackageRequest request) {
        Resp checkResp = ParamCheck.check(request, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return discountPackageService.findDiscountPackageByPage(request);
    }

    /**
     * 根据套餐ID查询详细
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/findDetailed")
    public Resp findPackageDetailed(DiscountPackageRequest request) {
        Resp checkResp = ParamCheck.check(request, "id");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return discountPackageService.findPackageDetailed(request);
    }

    /**
     * 修改套餐展示状态
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/updateParklotState")
    public Resp updateParklotState(DiscountPackageRequest request) {
        Resp checkResp = ParamCheck.check(request, "id", "parklotState", "parklotId");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return discountPackageService.updateParklotState(request);
    }

    /**
     * 修改套餐状态
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/updateState")
    public Resp updatePackageState(DiscountPackageRequest request) {
        Resp checkResp = ParamCheck.check(request, "id", "state");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return discountPackageService.updatePackageState(request);
    }

    /**
     * 新增/修改套餐详细
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/saveOrUpdate")
    public Resp saveOrUpdate(DiscountPackageRequest request) {
        Resp checkResp = ParamCheck.check(request, "name", "type", "state", "effectiveDaytime", "infoRule", "toplimit", "realQuantity", "packageAmount", "ruleType");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return discountPackageService.saveOrUpdate(request);
    }

    /**
     * 分页获取套餐统计列表
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/pageStaticlist")
    public Resp findPackageStaticByPage(DiscountPackageRequest request) {
        Resp checkResp = ParamCheck.check(request, "sEcho", "start", "length", "id");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return discountPackageService.findPackageStaticByPage(request);
    }

    /**
     * 套餐列表导出
     * @param response
     * @param request
     */
    @RequestMapping(value = "/excel")
    public void excel(HttpServletResponse response, DiscountPackageRequest request){
        if (ExcelUtil.paramCount(request) == Constants.EMPTY_CAPACITY) {
            throw new QhieWebException(Status.WebErr.EMPTY_EXCEL_PARAM);
        }
        // 下载文件的默认名称
        try {
            discountPackageService.excelPackage(request,response.getOutputStream(), DiscountPackageData.class);
            response.setHeader("Content-Disposition",
                    "attachment;filename="+ new String(("套餐列表" + ".xls").getBytes(), "iso-8859-1"));
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 套餐统计列表导出
     * @param response
     * @param request
     */
    @RequestMapping(value = "/excelStatic")
    public void excelStatic(HttpServletResponse response, DiscountPackageRequest request){
        if (ExcelUtil.paramCount(request) == Constants.EMPTY_CAPACITY) {
            throw new QhieWebException(Status.WebErr.EMPTY_EXCEL_PARAM);
        }
        // 下载文件的默认名称
        try {
            discountPackageService.excelStaticPackage(request,response.getOutputStream(), DiscountPackageStaticData.class);
            response.setHeader("Content-Disposition",
                    "attachment;filename="+ new String(("套餐统计列表" + ".xls").getBytes(), "iso-8859-1"));
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}

package com.qhieco.web.controller;

import com.qhieco.commonentity.Coupon;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.web.CouponRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.CouponData;
import com.qhieco.util.ExcelUtil;
import com.qhieco.util.ParamCheck;
import com.qhieco.webservice.CouponService;
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
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/3/28 19:42
 * <p>
 * 类说明：
 * 优惠券模块接口类
 */
@RestController
@RequestMapping("/coupon")
@Slf4j
public class CouponWeb {

    @Autowired
    CouponService couponService;

    /**
     * 商家优惠券兑换列表导出
     *
     * @param response
     * @param couponRequest
     */
    @RequestMapping(value = "/couponBusinessExcel")
    public void couponBusinessExcel(HttpServletResponse response, CouponRequest couponRequest) {
        if (ExcelUtil.paramCount(couponRequest) == Constants.EMPTY_CAPACITY) {
            throw new QhieWebException(Status.WebErr.EMPTY_EXCEL_PARAM);
        }
        // 下载文件的默认名称
        try {
            couponService.execl(couponRequest, response.getOutputStream(), Coupon.class);
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(("优惠券" + ".xls").getBytes(), "iso-8859-1"));
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 分页显示商家优惠券兑换列表
     *
     * @param couponRequest
     * @return
     */
    @PostMapping(value = "/couponBusinessAll")
    public Resp couponBusinessAll(CouponRequest couponRequest) {
        Resp checkResp = ParamCheck.check(couponRequest, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return couponService.query(couponRequest);
    }

    /**
     * 用户优惠券列表导出
     *
     * @param response
     * @param couponRequest
     */
    @RequestMapping(value = "/couponUserExcel")
    public void couponUserExcel(HttpServletResponse response, CouponRequest couponRequest) {
        if (ExcelUtil.paramCount(couponRequest) == Constants.EMPTY_CAPACITY) {
            throw new QhieWebException(Status.WebErr.EMPTY_EXCEL_PARAM);
        }
        // 下载文件的默认名称
        try {
            couponService.userCouponExecl(couponRequest, response.getOutputStream(), CouponData.class);
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(("优惠券" + ".xls").getBytes(), "iso-8859-1"));
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 分页显示用户优惠券列表
     *
     * @param couponRequest
     * @return
     */
    @PostMapping(value = "/couponUserAll")
    public Resp couponUserAll(CouponRequest couponRequest) {
        Resp checkResp = ParamCheck.check(couponRequest, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return couponService.userCouponQuery(couponRequest);
    }

    /**
     * 添加发放优惠券
     *
     * @param couponRequest
     * @return
     */
    @PostMapping(value = "/save")
    public Resp save(CouponRequest couponRequest) {
        log.info("添加发放优惠券方法接收参数：" + couponRequest);
        Resp checkResp = ParamCheck.check(couponRequest, "couponNum", "couponLimit", "beginTime",
                "endTime", "operationType");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        if (couponRequest.getOperationType().equals(Constants.DECIMAL_PLACE_DEFAULT)
                && couponRequest.getBusinessId() == null) {
            throw new ParamException(ParamCheck.check(couponRequest, "businessId"),
                    Status.WebErr.SYSTEM_ERROR.getCode());
        }
        if (Constants.EMPTY_CAPACITY == couponRequest.getUserIdList().size() &&
                Constants.EMPTY_CAPACITY == couponRequest.getTagList().size() &&
                couponRequest.getOperationType().equals(Constants.ANTI_GET_VERIFICATION) ) {
            throw new ParamException(ParamCheck.check(couponRequest, "userIdList"),
                    Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return couponService.save(couponRequest);
    }
}

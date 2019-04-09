package com.qhieco.apiwrite.web;


import com.qhieco.apiservice.DiscountPackageService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.constant.Status;
import com.qhieco.request.api.DiscountPackageOrderRequest;
import com.qhieco.request.api.DiscountPackageRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 黄金芽 327357731
 * @version 2.0.1 创建时间: 2018/7/18 13:28
 * <p>
 * 类说明：
 * 优惠套餐 controller
 */
@RestController
@RequestMapping("discount/package")
public class DiscountPackageWeb {

    @Autowired
    private DiscountPackageService discountPackageService;

    /**
     * 购买优惠套餐接口
     *
     * @param request
     * @return
     */
    @PostMapping("/savePackageOrder")
    public Resp savePackageOrder(@RequestBody DiscountPackageOrderRequest request)throws Exception {
        if (CommonUtil.isTimeStampInValid(request.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        return discountPackageService.savePackageOrder(request);
    }
}

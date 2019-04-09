package com.qhieco.apiwrite.web;

import com.qhieco.apiservice.CouponService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.constant.Status;
import com.qhieco.request.api.CouponBindRequest;
import com.qhieco.request.api.CouponExchangeRequest;
import com.qhieco.request.api.CouponIdRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.ParamCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/5 14:29
 * <p>
 * 类说明：
 * 卡卷接口层
 */

@RestController
@RequestMapping("/coupon")
public class  CouponWeb {


    @Autowired
    private CouponService couponService;

    @PostMapping("/exchange")
    public Resp exchange(@RequestBody CouponExchangeRequest couponExchangeRequest) {
        Resp resp = ParamCheck.check(couponExchangeRequest,  "user_id","coupon_code");
        if (!resp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return couponService.exchange(couponExchangeRequest);
    }

    @PostMapping("/active")
    public Resp active(@RequestBody CouponIdRequest couponIdRequest) {
        Resp resp = ParamCheck.check(couponIdRequest,  "coupon_id");
        if (!resp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return couponService.active(couponIdRequest);
    }

    @PostMapping("/bindOrderParking")
    public Resp bindOrderParking(@RequestBody CouponBindRequest couponBindRequest) {
        Resp resp = ParamCheck.check(couponBindRequest,  "coupon_id","order_parking_id");
        if (!resp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return couponService.bindOrderParking(couponBindRequest);
    }

}

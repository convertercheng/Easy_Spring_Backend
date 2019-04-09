package com.qhieco.apiread.web;

import com.qhieco.apiservice.CouponService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.constant.Status;
import com.qhieco.request.api.UserCouponRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.api.UserCouponListRepData;
import com.qhieco.response.data.api.UserCouponRepData;
import com.qhieco.util.CommonUtil;
import com.qhieco.util.RespUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 13:28
 * <p>
 * 类说明：
 * 优惠券controller
 */
@RestController
@RequestMapping("coupon")
public class CouponWeb {

    @Autowired
    private CouponService couponService;

    /**
     * 获取用户卡券列表
     *
     * @param request
     * @return
     */
    @PostMapping("get")
    public Resp couponList(@RequestBody UserCouponRequest request) {
        if (CommonUtil.isTimeStampInValid(request.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        if (request.getUser_id() == null || request.getPage_num() == null || request.getPage_num() < 0) {
            throw new QhieException(Status.ApiErr.PARAMS_COUPON_QUERY);
        }
        List<UserCouponRepData> userCouponRepData = couponService.queryUserCouponListByUserId(request.getUser_id(), request.getPage_num());
        UserCouponListRepData coupons = new UserCouponListRepData();
        coupons.setCoupons(userCouponRepData);
        return RespUtil.successResp(coupons);
    }
}

package com.qhieco.apiservice;

import com.qhieco.request.api.CouponBindRequest;
import com.qhieco.request.api.CouponExchangeRequest;
import com.qhieco.request.api.CouponIdRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.api.UserCouponRepData;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 11:36
 * <p>
 * 类说明：
 * 代金券service
 */
public interface CouponService {

    /**
     * 查询用户优惠券列表
     * @param userId
     * @param pageNum
     * @return
     */
    List<UserCouponRepData> queryUserCouponListByUserId(Integer userId, int pageNum);

    /**
     * 兑换优惠券
     * @param couponExchangeRequest
     * @return Resp
     */
    Resp exchange(CouponExchangeRequest couponExchangeRequest);


    /**
     * 激活已使用的优惠券
     * @param couponIdRequest
     * @return
     */
    Resp active(CouponIdRequest couponIdRequest);

    /**
     * 绑定优惠券
     * @param couponBindRequest
     * @return
     */
    Resp bindOrderParking(CouponBindRequest couponBindRequest);


}

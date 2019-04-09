package com.qhieco.apiservice.impl;

import com.qhieco.apiservice.CouponService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.commonentity.Coupon;
import com.qhieco.commonentity.OrderParking;
import com.qhieco.commonentity.relational.CouponOrderParkingB;
import com.qhieco.commonrepo.CouponOrderParkingBRepository;
import com.qhieco.commonrepo.CouponRepository;
import com.qhieco.commonrepo.OrderParkingRepository;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.mapper.CouponMapper;
import com.qhieco.request.api.CouponBindRequest;
import com.qhieco.request.api.CouponExchangeRequest;
import com.qhieco.request.api.CouponIdRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.api.UserCouponRepData;
import com.qhieco.util.CommonUtil;
import com.qhieco.util.RespUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 11:38
 * <p>
 * 类说明：
 * ${说明}
 */
@Service
@Slf4j
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponOrderParkingBRepository couponOrderParkingBRepository;

    @Autowired
    private OrderParkingRepository orderParkingRepository;

    @Override
    public List<UserCouponRepData> queryUserCouponListByUserId(Integer userId, int pageNum) {
        int startPage = pageNum <= 0 ? 0 : pageNum * Constants.PAGE_SIZE;
        return couponMapper.queryUserCouponListByUserId(userId, Status.Coupon.UNCLAIMED.getInt(), Status.Coupon.COUPON_CONVERTIBILITY.getInt(),
                Status.OrderParking.UNCONFIRMED.getInt(), Status.OrderParking.UNPAID.getInt(), startPage, Constants.PAGE_SIZE);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp exchange(CouponExchangeRequest couponExchangeRequest){
        //验证时间戳是否合法
        if (CommonUtil.isTimeStampInValid(couponExchangeRequest.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }

        String couponCode = couponExchangeRequest.getCoupon_code();
        Integer userId = couponExchangeRequest.getUser_id();
        int returnNum = couponRepository.exchange(couponCode, userId,Status.Coupon.COUPON_CONVERTIBILITY.getInt(),Status.Coupon.UNCLAIMED.getInt());
        if(Constants.EMPTY_CAPACITY == returnNum){
            //查询优惠券是否存在
            List<Coupon> coupons = couponRepository.findByCouponCode(couponCode);
            if(null != coupons && coupons.size() > 0){
                throw new QhieException(Status.ApiErr.REPEAT_COUPON_EXCHANGE);
            }
            throw new QhieException(Status.ApiErr.NONEXISTENT_COUPON_CODE);
        }
        return RespUtil.successResp();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp active(CouponIdRequest couponIdRequest){
        //验证时间戳是否合法
        if (CommonUtil.isTimeStampInValid(couponIdRequest.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        Integer couponId = couponIdRequest.getCoupon_id();
        //优惠券置为有效状态
        couponRepository.updateStateAndUseTimeAndUseMoneyById(couponId,Status.Coupon.COUPON_CONVERTIBILITY.getInt(),null,null);
        return RespUtil.successResp();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp bindOrderParking(CouponBindRequest couponBindRequest){
        //验证时间戳是否合法
        if (CommonUtil.isTimeStampInValid(couponBindRequest.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        Integer couponId = couponBindRequest.getCoupon_id();
        Integer orderParkingId = couponBindRequest.getOrder_parking_id();
        OrderParking orderParking = orderParkingRepository.findOne(orderParkingId);
        Integer orderState = orderParking.getState();
        if(!Status.OrderParking.UNCONFIRMED.getInt().equals(orderState) && !Status.OrderParking.UNPAID.getInt().equals(orderState)){
            throw new QhieException(Status.ApiErr.NOT_PAY_ORDER);
        }
        Long now = System.currentTimeMillis();
        //解绑之前的优惠券
        log.info("orderId:{}",orderParkingId);
        couponOrderParkingBRepository.updateStateByOrderId(orderParkingId,Status.Common.INVALID.getInt(),now);
        //couponId为-1即为解绑订单关联的优惠券
        if(couponId != Constants.DELETE_MARK){
            //查询优惠券是否可用
            Coupon coupon = couponRepository.findOne(couponId);
            if(null == coupon){
                throw new QhieException(Status.ApiErr.NONEXISTENT_COUPON);
            }
            if(!Status.Coupon.COUPON_CONVERTIBILITY.getInt().equals(coupon.getState())){
                throw new QhieException(Status.ApiErr.NONEXISTENT_COUPON);
            }
            //绑定优惠券
            //保存到优惠券停车订单关联表
            CouponOrderParkingB couponOrderParkingB = new CouponOrderParkingB(orderParkingId,couponId,now,now,Status.Common.VALID.getInt());
            couponOrderParkingBRepository.save(couponOrderParkingB);
        }
        return RespUtil.successResp();
    }

}

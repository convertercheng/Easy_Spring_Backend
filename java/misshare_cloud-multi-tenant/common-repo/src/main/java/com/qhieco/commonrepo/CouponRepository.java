package com.qhieco.commonrepo;

import com.qhieco.commonentity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/5 10:34
 * <p>
 * 类说明：
 *       优惠券表数据库交互层
 */

public interface CouponRepository extends JpaRepository<Coupon, Integer> ,JpaSpecificationExecutor<Coupon> {

    /**
     * 用户兑换优惠券
     * @param couponCode 优惠券code
     * @param userId 用户Id
     * @param state1 要改变成的状态
     * @param state2 当前状态
     */
    @Modifying
    @Query("update Coupon set mobileUserId = :userId,state = :state1 where couponCode = :couponCode and state = :state2")
    int exchange(@Param("couponCode") String couponCode, @Param("userId") Integer userId, @Param("state1") Integer state1, @Param("state2") Integer state2);

    /**
     * 根据code查询优惠券信息
     * @param couponCode
     * @return
     */
    List<Coupon> findByCouponCode(String couponCode);


    /**
     * 查询订单关联的优惠券code
     * @param orderId
     * @param state
     * @return
     */
    @Query("select couponCode from Coupon where id in (select couponId from CouponOrderParkingB where orderId = ?1 and state = ?2)")
    List<String> getCouponCodes(Integer orderId, Integer state);


    /**
     * 根据优惠券Id查询优惠券金额
     * @param couponId
     * @param state
     * @return
     */
    @Query("select c.couponLimit from Coupon c where  c.id = :couponId and c.state = :state")
    BigDecimal findCouponLimitByCouponId(@Param("couponId") Integer couponId, @Param("state") Integer state);



    /**
     * 根据订单id查询优惠券
     * @param orderId
     * @return
     */
    @Query("select c from Coupon c,CouponOrderParkingB cop where c.id = cop.couponId and cop.orderId = :orderId and cop.state = 1")
    Coupon findCouponByOrderId(@Param("orderId") Integer orderId);


    /**
     * 根据订单id查询优惠卷额度
     * @param orderId
     * @return
     */
    @Query(" select c.couponLimit from Coupon c,CouponOrderParkingB cop where c.id = cop.couponId and cop.orderId = :orderId and cop.state = 1")
    BigDecimal findCouponLimitByOrderId(@Param("orderId") Integer orderId);

    /**
     * 根据订单id修改优惠券状态
     * @param orderId
     * @param state
     * @param useTime
     * @param useMoney
     * @return
     */
    @Modifying
    @Query("update Coupon c set  c.state = :state, c.usedTime = :useTime, c.usedMoney = :useMoney where c.id = (select cop.couponId from CouponOrderParkingB cop where cop.orderId = :orderId and cop.state = 1)")
    void updateStateAndUseTimeAndUseMoney(@Param("orderId") Integer orderId, @Param("state") Integer state, @Param("useTime") Long useTime, @Param("useMoney") BigDecimal useMoney);


    /**
     * 根据id修改优惠券状态
     * @param couponId
     * @param state
     * @param useTime
     * @param useMoney
     * @return
     */
    @Modifying
    @Query("update Coupon c set  c.state = :state, c.usedTime = :useTime, c.usedMoney = :useMoney where c.id = :couponId")
    void updateStateAndUseTimeAndUseMoneyById(@Param("couponId") Integer couponId, @Param("state") Integer state, @Param("useTime") Long useTime, @Param("useMoney") BigDecimal useMoney);

    @Modifying
    @Query("DELETE from Coupon c where c.businessId=?1 and c.state=?2")
    void delCoupon(Integer businessId, Integer state);

    /**
     * 查询一段时间中优惠券的使用总额
     * @param startTime
     * @param endTime
     * @param state
     * @return
     */
    @Query("select sum(c.usedMoney) from Coupon c where c.usedTime between ?1 and ?2 and c.state = ?3")
    BigDecimal findSumUsedMoneyByUserTimeAndState(Long startTime, Long endTime, Integer state);


    /**
     * 查询优惠券的使用总额
     * @param state
     * @return
     */
    @Query("select sum(c.usedMoney) from Coupon c where c.state = ?1")
    BigDecimal findSumUsedMoneyByState(Integer state);


    /**
     * 查询一段时间中优惠券的发放总额
     * @param startTime
     * @param endTime
     * @return
     */
    @Query("select sum(c.couponLimit) from Coupon c where c.createTime between ?1 and ?2 and c.mobileUserId is not null")
    BigDecimal findSumLimitByCreateTime(Long startTime, Long endTime);


    /**
     * 查询优惠券的发放总额
     * @return
     */
    @Query("select sum(c.couponLimit) from Coupon c where c.mobileUserId is not null")
    BigDecimal findSumLimit();

    /**
     * 查询订单关联的优惠券code
     * @param orderId
     * @return
     */
    @Query("select couponCode from Coupon where id in (select couponId from CouponOrderParkingB where orderId = ?1) and state=1900")
    List<String> getCouponCodes(Integer orderId);


    /**
     * 修改已过期优惠券
     * @param now
     */
    @Modifying
    @Query("update Coupon c set c.state=1903 where c.endTime <= :now and c.state in(1901,1902)")
    void updateTimeOutCoupon(@Param("now") Long now);
}

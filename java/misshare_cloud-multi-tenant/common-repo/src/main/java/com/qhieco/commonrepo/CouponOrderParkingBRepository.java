package com.qhieco.commonrepo;

import com.qhieco.commonentity.relational.CouponOrderParkingB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author 许家钰 xujiayu0837@163.com
 * @version 2.0.1 创建时间: 18/3/8 上午11:13
 *          <p>
 *          类说明：
 */
public interface CouponOrderParkingBRepository extends JpaRepository<CouponOrderParkingB, Integer> {
    /**
     * 查询与订单关联的优惠券ID
     * @param orderId
     * @param state
     * @return
     */
    @Query("select id from CouponOrderParkingB where orderId = ?1 and state = ?2")
    List<Integer> getCouponIds(Integer orderId, Integer state);


    /**
     * 取消订单删除关联关系
     * @param orderId
     * @param couponId
     * @param state
     * @param modifyTime
     */
    @Modifying
    @Query("update CouponOrderParkingB set state = :state,modifyTime = :modifyTime where orderId = :orderId and couponId = :couponId")
    void cancel(@Param("orderId") Integer orderId, @Param("couponId") Integer couponId, @Param("state") Integer state, @Param("modifyTime") Long modifyTime);

    /**
     * 更新订单关联关系状态
     * @param orderId
     * @param state
     * @param modifyTime
     */
    @Modifying
    @Query("update CouponOrderParkingB set state = :state,modifyTime = :modifyTime where orderId = :orderId")
    void updateStateByOrderId(@Param("orderId") Integer orderId, @Param("state") Integer state, @Param("modifyTime") Long modifyTime);
}

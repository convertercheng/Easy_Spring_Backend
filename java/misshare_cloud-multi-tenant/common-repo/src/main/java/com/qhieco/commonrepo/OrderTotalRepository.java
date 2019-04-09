package com.qhieco.commonrepo;

import com.qhieco.commonentity.OrderTotal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/12 下午11:11
 * <p>
 * 类说明：
 *     OrderTotalRepository
 */

public interface OrderTotalRepository extends JpaRepository<OrderTotal, Integer> {

    /**
     * 根据订单号更新订单总表状态
     * @param serialNumber
     * @param state
     */
    @Modifying
    @Query("update OrderTotal set state = :state where serialNumber = :serialNumber")
    void updateStateBySerialNumber(@Param("serialNumber") String serialNumber, @Param("state") Integer state);


    /**
     * 根据订单号更新订单总表状态和订单金额
     * @param serialNumber
     * @param account
     * @param state
     */
    @Modifying
    @Query("update OrderTotal set account = :account,state = :state where serialNumber = :serialNumber")
    void updateStateAndAccountBySerialNumber(@Param("serialNumber") String serialNumber, @Param("account") BigDecimal account, @Param("state") Integer state);


    /**
     * 根据订单Id更新订单总表序列号
     * @param serialNumber
     * @param orderId
     */
    @Modifying
    @Query("update OrderTotal ot set ot.serialNumber = :serialNumber where ot.serialNumber = (select op.serialNumber from OrderParking op where op.id = :orderId)")
    void updateSerialNumberByOrderId(@Param("serialNumber") String serialNumber, @Param("orderId") Integer orderId);

}

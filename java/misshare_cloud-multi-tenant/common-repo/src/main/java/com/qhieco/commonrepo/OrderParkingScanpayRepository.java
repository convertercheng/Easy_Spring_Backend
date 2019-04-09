package com.qhieco.commonrepo;

import com.qhieco.commonentity.OrderParkingScanpay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/5/22 9:02
 * <p>
 * 类说明：
 * ${说明}
 */
public interface OrderParkingScanpayRepository extends JpaRepository<OrderParkingScanpay, Integer>, JpaSpecificationExecutor<OrderParkingScanpay> {
    /**
     * 根据订单号查询订单
     *
     * @param serialNumber
     * @return
     */
    OrderParkingScanpay findBySerialNumber(String serialNumber);

    @Modifying
    @Query(value = "update OrderParkingScanpay set tradeNo=:tradeNo, tripartiteFee=:tripartiteFee, payTime=:payTime, state=:state where id=:id")
    public void updateInfo(@Param("id") Integer id, @Param("tradeNo") String tradeNo, @Param("tripartiteFee") BigDecimal tripartiteFee,
                           @Param("state") Integer state, @Param("payTime") Long payTime);

    @Query(value = "SELECT pay_time FROM t_order_parking_scanpay tops WHERE tops.plate_no=:plateNo AND tops.pay_time IS NOT NULL AND tops.`state`=1304 " +
            " AND tops.pay_time<=UNIX_TIMESTAMP(NOW())*1000 AND (tops.pay_time+:outTime)>UNIX_TIMESTAMP(NOW())*1000 " +
            " AND tops.`real_start_time`=:enterTime ORDER BY id DESC LIMIT 1", nativeQuery = true)
    public Long queryPayTimeByPlateNo(@Param("plateNo") String plateNo, @Param("outTime") Long outTime, @Param("enterTime") Long enterTime);
}

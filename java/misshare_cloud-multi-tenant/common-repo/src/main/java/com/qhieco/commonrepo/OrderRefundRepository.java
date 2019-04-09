package com.qhieco.commonrepo;

import com.qhieco.commonentity.OrderRefund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/5 10:34
 * <p>
 * 类说明：
 *       退款表数据库交互层
 */

public interface OrderRefundRepository extends JpaRepository<OrderRefund, Integer>,JpaSpecificationExecutor<OrderRefund> {


    /**
     * 某一时间段内所有退款金额总和
     * @param startTime
     * @param endTime
     * @param state
     * @return
     */
    @Query("select sum(o.fee)from OrderRefund o where o.updateTime between ?1 and ?2 and o.state = ?3")
    BigDecimal sumOfFeeByUpdateTimeAndState(Long startTime, Long endTime, Integer state);


    /**
     * 所有退款金额总和
     * @param state
     * @return
     */
    @Query("select sum(o.fee)from OrderRefund o where o.state = ?1")
    BigDecimal sumOfFeeByState(Integer state);

    @Query("select count(orf.id) from OrderRefund orf, OrderParking op where orf.orderParkingId = op.id and op.mobileUserId = ?1 and orf.state in ?2")
    Integer findByUserIdAndState(Integer id, Integer state);
}

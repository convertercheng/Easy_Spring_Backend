package com.qhieco.commonrepo.iot;

import com.qhieco.commonentity.BalanceParklot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/4/23 下午9:32
 * <p>
 * 类说明：
 * ${description}
 */

public interface BalanceParklotRepository extends JpaRepository<BalanceParklot, Integer> {

    /**
     * 根据车场Id更新车场的收入
     * @param parklotId
     * @param income
     */
    @Modifying
    @Query("update BalanceParklot set balance=balance+:income where parklotId = :parklotId")
    void updateBalanceByParklotId(@Param("parklotId") Integer parklotId, @Param("income") BigDecimal income);
}

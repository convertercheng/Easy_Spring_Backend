package com.qhieco.commonrepo;

import com.qhieco.commonentity.ParklotAmount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/26 10:34
 * <p>
 * 类说明：
 *       停车场总数表数据库交互层
 */

public interface ParklotAmountRepository extends JpaRepository<ParklotAmount, Integer> {

    /**
     * 根据停车场查询停车位数量
     * @param parklotId 停车场id
     * @return ParklotAmount
     */
    @Query("select p from ParklotAmount p where p.parklotId = :parklotId")
    ParklotAmount findByParklotId(@Param("parklotId") Integer parklotId);

    @Query(value = "select p.reservableAmount from ParklotAmount p where p.parklotId=:parklotId")
    Integer findReservableAmountByParklotId(@Param("parklotId") Integer parklotId);

    @Modifying
    @Query("update ParklotAmount set publishAmount = publishAmount + :publishNumber,idleAmount = idleAmount + :publishNumber,modifyTime = :modifyTime where parklotId = :parklotId")
    void publish(@Param("parklotId") Integer parklotId, @Param("publishNumber") Integer publishNumber, @Param("modifyTime") Long modifyTime);

    @Modifying
    @Query("update ParklotAmount set reservedAmount = reservedAmount + :reserveNumber,idleAmount = idleAmount - :reserveNumber,modifyTime = :modifyTime where parklotId = :parklotId")
    void reserve(@Param("parklotId") Integer parklotId, @Param("reserveNumber") Integer reserveNumber, @Param("modifyTime") Long modifyTime);

    @Modifying
    @Query("update ParklotAmount set reservedAmount = reservedAmount + :reserveNumber,idleAmount = idleAmount - :reserveNumber,modifyTime = :modifyTime where parklotId = (select pc.parklotId from Parkloc pc where id = :parklocId)")
    void reserveByParklocId(@Param("parklocId") Integer parklocId, @Param("reserveNumber") Integer reserveNumber, @Param("modifyTime") Long modifyTime);

    /**
     * 根据parklot id查询停车区车位数量信息
     * @param parklotIds 停车区id
     * @return
     */
    List<ParklotAmount> findByParklotIdIn(List<Integer> parklotIds);

}

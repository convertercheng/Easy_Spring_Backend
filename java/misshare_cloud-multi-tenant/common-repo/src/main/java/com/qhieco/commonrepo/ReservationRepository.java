package com.qhieco.commonrepo;

import com.qhieco.commonentity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/26 10:34
 * <p>
 * 类说明：
 *       停车位数据库交互层
 */

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    /**
     * 根据Ids查询预约记录
     * @param ids
     * @return
     */
    @Query("select r from Reservation r where r.id in(:ids)")
    List<Reservation> findByIds(@Param("ids") List<Integer> ids);


}

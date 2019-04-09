package com.qhieco.commonrepo;

import com.qhieco.commonentity.Publish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/5 10:34
 * <p>
 * 类说明：
 *       发布表数据库交互层
 */

public interface PublishRepository extends JpaRepository<Publish, Integer> {

    /**
     * 查询一个车位下所有有效的发布时间
     * @param parklocId
     * @param state
     * @return
     */
    List<Publish> findByParklocIdAndState(Integer parklocId, Integer state);

    /**
     * 查询一个车位下除失效和修改之前的发布之外所有的发布时间
     * @param parklocId
     * @param state
     * @return
     */
    List<Publish> findByParklocIdAndStateNotAndIdNot(Integer parklocId, Integer state, Integer id);


    /**
     * 根据车位Id和状态查询发布
     * @param parklocId
     * @param states
     * @return
     */
    @Query("select pu from Publish pu where pu.parklocId=:parklocId and state in(:states)")
    List<Publish> findByParklocIdAndStates(@Param("parklocId") Integer parklocId, @Param("states") List<Integer> states);

    @Modifying
    @Query("update Publish set state = :state where id = :id")
    void updateState(@Param("id") Integer id, @Param("state") Integer state);

    /**
     * 根据车位Id改变状态
     * @param id
     * @param state1
     * @param state2
     * @return
     */
    @Modifying
    @Query("update Publish set state = :state1 where id = :id and state = :state2")
    void changeStateByIdAndState(@Param("id") Integer id, @Param("state1") Integer state1, @Param("state2") Integer state2);

    @Modifying
    @Query("update Publish set state = :state,lastStartTime = startTime,lastEndTime = endTime,lastTimeRange = timeRange,lastMode = mode,lastDayOfWeek = dayOfWeek,startTime = :startTime,endTime = :endTime,timeRange = :timeRange,mode = :mode,dayOfWeek = :dayOfWeek,state = :state where id = :id")
    void alterPublish(@Param("id") Integer id, @Param("startTime") Long startTime, @Param("endTime") Long endTime, @Param("timeRange") String timeRange, @Param("mode") Integer mode, @Param("dayOfWeek") String dayOfWeek, @Param("state") Integer state);


    @Modifying
    @Query("update Publish set state = :state,startTime = :startTime,endTime = :endTime,timeRange = :timeRange,mode = :mode,dayOfWeek = :dayOfWeek,state = :state where id = :id")
    void alterSecondPublish(@Param("id") Integer id, @Param("startTime") Long startTime, @Param("endTime") Long endTime, @Param("timeRange") String timeRange, @Param("mode") Integer mode, @Param("dayOfWeek") String dayOfWeek, @Param("state") Integer state);

    /**
     * 通过预约Id查询发布
     * @param reservationId
     * @return
     */
    @Query(value = "SELECT p.* from t_publish p,t_share s,t_reservation rn where p.id=s.publish_id and s.id=rn.share_id and rn.id = :reservationId",nativeQuery = true)
    Publish findByReservationId(@Param("reservationId") Integer reservationId);

    List<Publish> findByParklocIdAndStateNot(Integer parklocId, Integer state);

}

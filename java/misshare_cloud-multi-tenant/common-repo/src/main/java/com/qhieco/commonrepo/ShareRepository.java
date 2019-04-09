package com.qhieco.commonrepo;

import com.qhieco.commonentity.Parkloc;
import com.qhieco.commonentity.Share;
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
 *       分享表数据库交互层
 */

public interface ShareRepository extends JpaRepository<Share, Integer> {

    /**
     * 查询车场下能被预约的车位
     * @param parklocId
     * @param state
     * @return List<Share>
     *
     */
    List<Share> findByParklocIdAndState(Integer parklocId, Integer state);




    /**
     * 查询车场下某个时间段能被预约的车位
     * @param parklotId
     * @param state
     * @param startTime
     * @return List<Share>
     *
     */
    @Query("select s from Share s where s.parklocId in(select p.id from Parkloc p where p.parklotId = :parklotId and p.state=:state) and s.startTime <= :startTime and s.endTime >= :startTime and s.state = 1 order by s.endTime desc")
    List<Share> findByParklotIdAndStateAndStartTime(@Param("parklotId") Integer parklotId, @Param("state") Integer state, @Param("startTime") Long startTime);

    /**
     * 查询车场下某个时间段能被预约的车位
     * @param parklotId
     * @param state
     * @param startTime
     * @param endTime
     * @return List<Share>
     *
     */
    @Query("select s from Share s where s.parklocId in(select p.id from Parkloc p where p.parklotId = :parklotId and p.state=:state) and s.startTime = :startTime and s.endTime = :endTime and s.state = 1")
    List<Share> findByParklotIdAndStateAndStartTimeAndEndTime(@Param("parklotId") Integer parklotId, @Param("state") Integer state, @Param("startTime") Long startTime, @Param("endTime") Long endTime);


    /**
     * 查询车位该时间段可被预约的共享时段
     * @param districtId
     * @param state
     * @param startTime
     * @param endTime
     * @return List<Share>
     *
     */
    @Query("select s from Share s where s.parklocId in(select p.id from Parkloc p where p.parklotDistrictId = :districtId and p.state=:state) and s.startTime = :startTime and s.endTime = :endTime and s.state = 1")
    List<Share> findByDistrictIdAndStateAndStartTimeAndEndTime(@Param("districtId") Integer districtId, @Param("state") Integer state, @Param("startTime") Long startTime, @Param("endTime") Long endTime);

    /**
     * 查询车位该时间段可被预约的共享时段
     * @param parklocId
     * @param state
     * @param startTime
     * @param endTime
     * @return List<Share>
     *
     */
    @Query("select s from Share s where s.parklocId in(select p.id from Parkloc p where p.id = :parklocId and p.state=:state) and s.startTime = :startTime and s.endTime = :endTime and s.state = 1")
    List<Share> findByParklocIdAndStateAndStartTimeAndEndTime(@Param("parklocId") Integer parklocId, @Param("state") Integer state, @Param("startTime") Long startTime, @Param("endTime") Long endTime);


    /**
     * 根据发布Id改变状态
     * @param publishId
     * @param state1 要改变成的状态
     * @param state2 原本的状态
     */
    @Modifying
    @Query("update Share set state = :state1 where publishId = :publishId and state = :state2")
    void updateState(@Param("publishId") Integer publishId, @Param("state1") Integer state1, @Param("state2") Integer state2);



}

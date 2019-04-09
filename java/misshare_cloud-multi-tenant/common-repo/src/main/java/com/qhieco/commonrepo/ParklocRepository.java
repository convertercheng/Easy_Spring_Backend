package com.qhieco.commonrepo;

import com.qhieco.commonentity.Parkloc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/26 10:34
 * <p>
 * 类说明：
 *       停车位数据库交互层
 */

public interface ParklocRepository extends JpaRepository<Parkloc, Integer> ,JpaSpecificationExecutor<Parkloc> {

    /**
     * 查询车场下能被预约的车位
     * @param parklotId
     * @param state
     * @return List<Parkloc>
     *
     */
    List<Parkloc> findByParklotIdAndState(Integer parklotId, Integer state);

    /**
     * 根据车位Id和用户Id获取有效的车位
     * @param id
     * @param mobileUserId
     * @param state
     * @return
     */
    @Query("select p from Parkloc p where p.id = :id and p.mobileUserId = :mobileUserId and state <> :state")
    Parkloc findByIdAndMobileUserIdAndState(@Param("id") Integer id, @Param("mobileUserId") Integer mobileUserId, @Param("state") Integer state);


    /**
     * 根据发布Id查询车位
     * @param publishId
     * @param state
     * @return
     */
    @Query("select p from Parkloc p where p.id = (select pu.parklocId from Publish pu where pu.id = :publishId and pu.state <> :state)")
    Parkloc findByPublishId(@Param("publishId") Integer publishId, @Param("state") Integer state);

    /**
     * 改变车位状态
     * @param parklocId
     * @param state
     */
    @Modifying
    @Query("update Parkloc set state = :state where id = :parklocId")
    void updateState(@Param("parklocId") Integer parklocId, @Param("state") Integer state);


    /**
     * 改变待发布的车位状态
     * @param parklocId
     * @param state
     */
    @Modifying
    @Query("update Parkloc set state = :state where id = :parklocId and state=1100")
    void updateStateByParklocId(@Param("parklocId") Integer parklocId, @Param("state") Integer state);


    /**
     * 改变车位状态
     * @param parklocIds
     * @param state
     */
    @Modifying
    @Query("update Parkloc set state = :state where id in(:parklocIds) and state=1100")
    void updateStateByParklocIds(@Param("parklocIds") List<Integer> parklocIds, @Param("state") Integer state);

    /**
     * 根据小区名查找车位id
     * @param name
     * @return
     */
    @Query("select id from Parkloc where parklotId in (select id from Parklot where name like :name)")
    List<Integer> findByParklotName(@Param("name") String name);

    /**
     * 根据车位管理员手机号查找车位id
     * @param phone
     * @return
     */
    @Query("select p.id from Parkloc p where p.mobileUserId in (select u.id from UserMobile u where u.phone like ?1)")
    List<Integer> findIdsByPhoneLike(String phone);

    /**
     * 根据车位管理员姓名查找车位id
     * @param phone
     * @return
     */
    @Query("select p.id from Parkloc p where p.mobileUserId in (select u.id from UserMobile u where u.name like ?1)")
    List<Integer> findIdsByUserNameLike(String phone);

    /**
     * 根据小区类型查找车位id
     * @param parklotType
     * @return
     */
    @Query("select pc.id from Parkloc pc where pc.parklotId in (select pt.id from Parklot pt where pt.type = ?1)")
    List<Integer> findIdsByParklotType(Integer parklotType);

    /**
     * 根据用户id获取所有的车位id
     * @param userId 用户id
     * @return 返回该用户手下的车位
     */
    @Query("select id from Parkloc where mobileUserId = :userId")
    List<Integer> findByMobileUserId(@Param("userId") Integer userId);

    /**
     * 根据Ids查询车位
     * @param ids
     * @return
     */
    @Query("select p from Parkloc p where p.id in(:ids)")
    List<Parkloc> findByIds(@Param("ids") List<Integer> ids);

    /**
     * 增加悲观锁的findOne方法　用于处理订单并发问题
     * @param id
     * @return
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Parkloc p where p.id = ?1")
    Parkloc findOneLocked(Integer id);

    /**
     * 根据车场ID查询车位数
     * @param parklotId
     * @return
     */
    @Query("select count(p.id) from Parkloc p where p.parklotId=?1 ")
    Integer findByParklocCount(Integer parklotId);

    Parkloc findByParklotDistrictId(Integer parklotDistrictId);
}

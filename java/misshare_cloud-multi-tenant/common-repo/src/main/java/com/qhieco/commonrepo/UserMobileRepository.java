package com.qhieco.commonrepo;

import com.qhieco.commonentity.UserMobile;
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
 * 用户数据库交互层
 */

public interface UserMobileRepository extends JpaRepository<UserMobile, Integer> {
    /**
     * 根据手机号码查找用户
     *
     * @param phone 手机号码
     * @return UserMobile bean
     */
    UserMobile findByPhone(String phone);

    /**
     * 通过用户id更新用户手机号码
     *
     * @param phone
     * @param id
     */
    @Modifying
    @Query("update UserMobile set phone = :phone where id = :id")
    void shift(@Param("phone") String phone, @Param("id") Integer id);

    /**
     * 根据手机号码模糊查询用户
     *
     * @param phone 手机号码
     * @return Id集合
     */
    @Query("select u.id from UserMobile u where u.phone like :phone")
    List<Integer> findByPhoneIds(@Param("phone") String phone);

    /**
     * 修改用户积分
     *
     * @param userId
     * @param integral
     */
    @Modifying
    @Query(value = "update UserMobile set integral = ?2 where id = ?1")
    void updateUserMobilePlusIntegral(Integer userId, Integer integral);


    /**
     * 根据车位Id查出所属用户信息
     *
     * @param parklocId
     */
    @Query(value = "select u from UserMobile u where u.id = (select c.mobileUserId from Parkloc c where c.id = ?1)")
    UserMobile findByParklocId(Integer parklocId);

    /**
     * 修改用户首次下单字段
     *
     * @param userId
     */
    @Modifying
    @Query(value = "update UserMobile set isIndexOrder = 1 where id = ?1")
    void updateUserMobileIndexOrder(Integer userId);
}

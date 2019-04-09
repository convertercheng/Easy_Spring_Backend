package com.qhieco.commonrepo;

import com.qhieco.commonentity.UserExtraInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/2/26 下午10:42
 * <p>
 * 类说明：
 *     UserExtraInfoRepository
 */

public interface UserExtraInfoRepository extends JpaRepository<UserExtraInfo, Integer> {

    /**
     * 通过userId找到三方信息
     * @param userId 用户id
     * @return UserExtraInfo实例
     */
    UserExtraInfo findByMobileUserId(int userId);


    /**
     * 更新阿里支付信息
     * @param alipayUserId
     * @param alipayLogonId
     * @param userId
     */
    @Modifying
    @Query("update UserExtraInfo set alipayUserId = :alipayUserId ,alipayLogonId = :alipayLogonId where mobileUserId = :userId")
    void updateAlipayInfo(@Param("alipayUserId") String alipayUserId, @Param("alipayLogonId") String alipayLogonId, @Param("userId") Integer userId);

    /**
     * 更新微信支付信息
     * @param wxOpenId
     * @param userId
     */
    @Modifying
    @Query("update UserExtraInfo set wxOpenId = :wxOpenId  where mobileUserId = :userId")
    void updateWxpayInfo(@Param("wxOpenId") String wxOpenId, @Param("userId") Integer userId);


    /**
     * 更新重复的极光ID
     * @param mobileUserId
     * @param jpushRegId
     */
    @Transactional
    @Modifying
    @Query("update UserExtraInfo set jpushRegId = '' where mobileUserId <> :mobileUserId and jpushRegId = :jpushRegId ")
    void updateSameJpushRegId(@Param("mobileUserId") Integer mobileUserId, @Param("jpushRegId") String jpushRegId);


    /**
     * 通过userIds找到三方信息
     * @param userIds 用户ids
     * @return UserExtraInfo实例
     */
    @Query("select u from UserExtraInfo u where u.mobileUserId in(:userIds)")
    List<UserExtraInfo> findByMobileUserIds(@Param("userIds") List<Integer> userIds);

    @Query("select  u from UserExtraInfo u where u.mobileUserId=?1")
    UserExtraInfo findByUserExtraInfoId(Integer userId);

    @Query("select  u.wxUnionId from UserExtraInfo u where u.mobileUserId=?1 and u.wxUnionId is not null ")
    String findByUserExtraInfo(Integer userId);

    @Query("select  u.wxBindOpenId from UserExtraInfo u where u.mobileUserId=?1 and u.wxBindOpenId is not null ")
    String findByWxBindOpenId(Integer userId);

    @Query("select  u from UserExtraInfo u where u.wxUnionId=?1")
    UserExtraInfo findByUserExtraInfo(String unionId);

    @Query("select  u from UserExtraInfo u where u.mobileUserId=?1")
    UserExtraInfo findUserExtraInfo(Integer userId);

    @Query("select  u from UserExtraInfo u where u.wxBindOpenId=?1 and u.mobileUserId is not null")
    UserExtraInfo findByWxBindOpenId(String wxBindOpenId);

}

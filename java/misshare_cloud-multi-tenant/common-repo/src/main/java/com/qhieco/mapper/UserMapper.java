package com.qhieco.mapper;

import com.qhieco.commonentity.Plate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 14:48
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface UserMapper {

    BigDecimal queryUserWithdrawAmountByUserId(Integer userId);

    @Select(value = "SELECT tbp.`balance` FROM t_balance_parklot tbp INNER JOIN t_parklot tpt ON tpt.`id`=tbp.`parklot_id`  WHERE tpt.`mobile_user_id`=#{userId}")
    BigDecimal queryParklotAmountByUserId(@Param("userId") Integer userId);

    /**
     * 根据用户id查询用户类型
     *
     * @param userId 用户id
     * @return 返回用户类型
     */
    Integer queryUserTypeByUserId(Integer userId);

    /**
     * 根据用户id查询用户头像路径
     *
     * @param userId 用户id
     * @return 返回用户头像路径
     */
    String queryAvatarPathByUserId(Integer userId);

    /**
     * 根据用户id查询用户预约的时段
     *
     * @param userId 用户ID
     * @return 返回用户的预约时间
     */
    String queryReservationPeriodByUserId(Integer userId);

    @Update(value = "UPDATE t_balance_user SET balance_earn = balance_earn + #{ownerIncome} WHERE mobile_user_id = " +
            "(SELECT tpc.mobile_user_id from t_parkloc tpc INNER JOIN t_order_parking top ON top.parkloc_id=tpc.id WHERE top.id=#{orderId} LIMIT 1)")
    void updateOwnerBalanceByOrderId(@Param("orderId") int orderId, @Param("ownerIncome") BigDecimal ownerIncome);


    @Update(value = "UPDATE t_balance_user SET balance_earn = balance_earn + #{manageIncome} WHERE mobile_user_id = #{manageUserId}")
    void updateManageBalanceByOrderId(@Param("manageUserId") int manageUserId, @Param("manageIncome") BigDecimal manageIncome);

    /**
     * 根据用户名查询车牌信息
     *
     * @param userId 用户id
     * @return 车牌信息
     */
    Plate queryPlateByUserId(@Param("user_id") Integer userId);

    HashMap<String, Object> queryUserInfoByUserId(Integer userId);
}


package com.qhieco.mapper;

import com.qhieco.response.data.api.UserCouponRepData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 11:17
 * <p>
 * 类说明：
 * 用户代金券mapper
 */
@Mapper
public interface CouponMapper {
    /**
     * 查询用户代金券列表
     *
     * @param userId               用户id
     * @param unclaimed            代金券状态
     * @param couponConvertibility 优惠券已兑换状态
     * @param unconfirmed          订单未确认状态
     * @param unpaid               订单未支付状态
     * @param startPage            开始页数
     * @param pageSize             每页展示条数
     * @return
     */
    List<UserCouponRepData> queryUserCouponListByUserId(@Param("userId") Integer userId, @Param("unclaimed") int unclaimed,
                                                        @Param("couponConvertibility") int couponConvertibility,
                                                        @Param("unconfirmed") int unconfirmed, @Param("unpaid") int unpaid,
                                                        @Param("startPage") int startPage,
                                                        @Param("pageSize") int pageSize);

    /**
     * 查询用户代金券数量
     *
     * @param userId 用户id
     * @param state  状态
     * @return
     */
    int queryCountUserCouponByUserId(@Param("userId") Integer userId, @Param("state") int state);
}

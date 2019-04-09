package com.qhieco.mapper;

import com.qhieco.commonentity.Coupon;
import com.qhieco.commonentity.LogLock;
import com.qhieco.response.data.api.*;
import com.qhieco.time.OrderMessageInfo;
import com.qhieco.time.ReserveOrderInfo;
import com.qhieco.time.UnconfirmedOrderInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/2/28 16:13
 * <p>
 * 类说明：
 * 订单的mapper
 */
@Mapper
public interface OrderMapper {

    OrderUsingRepData queryOrderUsing(HashMap<String, Object> param);

    /**
     * 查询车主的账单列表
     *
     * @param params
     * @return
     */
    List<BillRepData> queryCarOwnerBillListByUserId(HashMap<String, Object> params);

    /**
     * 业主的账单列表
     *
     * @param params
     * @return
     */
    List<BillRepData> queryOwnerBillListByUserId(HashMap<String, Object> params);

    /**
     * 物业(管理员)的账单列表
     *
     * @param params
     * @return
     */
    List<BillRepData> queryAdminBillListByUserId(HashMap<String, Object> params);


    BillDetailRepData queryBillDetailBySerialNumber(@Param("serialNumber") String serialNumber, @Param("type") int type,
                                                    @Param("userType") Integer userType);

    List<InvoiceOrderRepData> queryInvoiceOrderListByCondition(@Param("userId") Integer userId, @Param("invoiceState") Integer invoiceState,
                                                               @Param("startPage") int startPage, @Param("pageSize") int pageSize);

    @Select(value = "select reserve_id reserveId, state from t_order_parking where id=#{orderId}")
    HashMap queryStateByOrderId(Integer orderId);

    /**
     * 根据orderid查询用户的信息
     *
     * @param orderId 订单id
     * @param state   状态
     * @return 预约订单详情
     */
    ReserveOrderDetailRespData queryReserveOrderByOrderId(@Param("orderId") Integer orderId, @Param("state") Integer state);

    ReserveOrderDetailRespData queryParkingOrderByOrderId(@Param(value = "orderId") Integer orderId);
    
    List<ReserveOrderRespData> queryReserveOrderListByUserId(@Param("userId") Integer userId,
                                                             @Param("startPage") Integer startPage,
                                                             @Param("pageSize") Integer PageSize);

    /**
     * 获取业主的订单管理列表
     * @param params
     * @return
     */
    List<OrderManageRespData> queryOwnerOrderManageListByCondition(HashMap params);

    /**
     * 获取管理员的订单管理列表
     * @param params
     * @return
     */
    List<OrderManageRespData> queryManagerOrderManageListByCondition(HashMap params);

    OrderManageDetailRespData queryOrderManageDetailByOrderId(@Param("userType") Integer userType, @Param("orderId") Integer orderId);

    HashMap queryReserveCouponInfoByParkingOrderId(@Param("orderId") int orderId, @Param("valid") int valid);

    List<UnconfirmedOrderInfo> queryAllUnconfirmedOrderInfo(@Param("unconform") int unconform, @Param("autoCancelReservedTime") long autoCancelReservedTime);

    @Update(value = "update t_order_parking set state=#{state} where id=#{orderId}")
    void updateOrderStateById(@Param("orderId") int orderId, @Param("state") int state);

    @Update(value = "update t_order_parking set state = #{orderState}, invoice_state=#{invoiceState} where id=#{orderId}")
    void updateOrderStateAdInvoiceStateById(@Param("orderId") int orderId, @Param("orderState") int state, @Param("invoiceState") int invoiceState);

    List<ReserveOrderInfo> queryReserveOrderList(@Param("reserve") int reserve, @Param("valid") int valid,
                                                 @Param("maxDelayTimeKey") String maxDelayTimeKey);

    /**
     * 查出极光推送需要的相关信息
     *
     * @param orderId
     * @return
     */
    OrderMessageInfo queryOrderMessageInfo(@Param(value = "orderId") Integer orderId);

    /**
     * 根据订单id查询优惠券信息
     *
     * @param orderId 订单id
     * @return 返回优惠券信息
     */
    Coupon queryCouponByOrderId(@Param("orderId") Integer orderId);

    HashMap queryInvoiceInfoByOrderId(@Param("orderId") Integer orderId);

    @Select(value = "SELECT id, state FROM t_order_parking WHERE reserve_id =#{reserveId} order by id desc limit 1")
    HashMap queryParkingStateByReserveId(@Param("reserveId") Integer reserveId);

    @Select(value = "SELECT parkloc_id parklocId from t_order_parking top  INNER JOIN t_plate tp " +
            " ON top.plate_id=tp.id WHERE number=#{plateNo} AND tp.state='1' AND top.state='1302'  ORDER BY top.id desc LIMIT 1")
    Integer queryParklocIdByPlateNo(@Param("plateNo") String plateNo);

    @Select(value = "SELECT top.id from t_order_parking top  INNER JOIN t_plate tp " +
            " ON top.plate_id=tp.id WHERE number=#{plateNo} AND tp.state='1' AND top.state='1302'  ORDER BY top.id desc LIMIT 1")
    Integer queryOrderIdByPlateNo(@Param("plateNo") String plateNo);

    @Update("update t_order_total set state=#{state}, update_time=UNIX_TIMESTAMP()*1000  where serial_number = #{serialNumber}")
    void updateOrderTotalStateBySerialNumber(@Param("serialNumber") String serialNumber, @Param("state") int state);

    @Update("UPDATE t_order_total SET state=#{state} WHERE serial_number=(SELECT serial_number FROM t_order_parking WHERE id=#{orderId})")
    void updateOrderTotalStateByOrderId(@Param("orderId") Integer orderId, @Param("state") int state);

    @Select(value = "SELECT tpt.mobile_user_id userId, tpt.id parklotId FROM t_parklot tpt INNER JOIN t_parkloc tpc ON tpc.parklot_id=tpt.id INNER JOIN t_order_parking top" +
            " ON top.parkloc_id=tpc.id WHERE top.id=#{orderId} LIMIT 1")
    HashMap<String, Integer> queryManageUserIdAdParklotIdByOrderId(@Param("orderId") Integer orderId);

    @Update(value = "UPDATE t_order_parking  top SET top.`total_fee`=#{totalFee}, top.`overtime_fee`=#{overtimeFee}, top.`third_party_no`=#{thridPartyNo}," +
            " top.`real_end_time`=#{now} , top.`overtime`=#{overtime} WHERE top.`id`=#{orderId}")
    public void updateOrderById(@Param("orderId") Integer orderId, @Param("totalFee") BigDecimal totalFee,
                                @Param("overtimeFee") BigDecimal overtimeFee, @Param("thridPartyNo") String thridPartyNo,
                                @Param("overtime") Long overtime, @Param("now") Long now);

    @Update(value = "UPDATE t_order_parking SET real_end_time = UNIX_TIMESTAMP(NOW())*1000 WHERE id=#{orderId}")
    public void updateEndTimeById(@Param("orderId") Integer orderId);

    public ScanpayOrderInfo queryParkingInfoByPlateNoAdParklotId(@Param("parklotId") Integer parklotId, @Param("plateNo") String plateNo);

    public ScanpayOrderInfo queryReserveInfoByPlateNoAdParklotId(@Param("parklotId") Integer parklotId, @Param("plateNo") String plateNo);

}

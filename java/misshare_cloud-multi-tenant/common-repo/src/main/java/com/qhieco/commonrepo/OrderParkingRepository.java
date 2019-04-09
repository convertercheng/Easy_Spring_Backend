package com.qhieco.commonrepo;

import com.qhieco.commonentity.OrderParking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 下午2:51
 * <p>
 *    订单Dao
 *
 */

public interface OrderParkingRepository extends JpaRepository<OrderParking,Integer>, JpaSpecificationExecutor<OrderParking> {

    /**
     * 查询订单是否已经开票
     * @param ids
     * @param invoiceState
     * @return
     */
    @Query("select o from OrderParking o where o.invoiceState = :invoiceState and o.id in (:ids)")
    List<OrderParking> findOrderParkingByIdsAndInvoiceState(@Param(value = "ids") List<Integer> ids, @Param("invoiceState") Integer invoiceState);

    /**
     * 查询订单列表是否存在
     * @param ids
     * @return
     */
    @Query("select o from OrderParking o where o.id in (:ids)")
    List<OrderParking> findOrderParkingByIds(@Param(value = "ids") List<Integer> ids);

    /**
     * 修改订单的发票状态
     * @param ids
     * @param invoiceState
     */
    @Modifying
    @Query("update OrderParking set invoiceState = :invoiceState where id in (:ids)")
    void updateInvoiceStateById(@Param(value = "ids") List<Integer> ids, @Param("invoiceState") Integer invoiceState);


    /**
     * 查询某个用户的未完成订单
     * @param mobileUserId
     * @param states
     * @return
     */
    @Query(value = "select count(o.id) + (select count(top.id) from t_order_parking top where top.id not in(select topreserve.id from t_order_parking topreserve,t_order_parking topparking where topreserve.id = topparking.reserve_id) and top.mobile_user_id = :mobileUserId and top.state = :state) from t_order_parking o  where o.mobile_user_id = :mobileUserId and o.state in (:states)",nativeQuery = true)
    Integer findOrderParkingByMobileUserIdAndStates(@Param("mobileUserId") Integer mobileUserId, @Param(value = "states") List<Integer> states, @Param("state") Integer state);

    /**
     * 查询某个用户的未完成订单
     * @param mobileUserId
     * @param states
     * @return
     */
    @Query(value = "select o.* from t_order_parking o  where o.mobile_user_id = :mobileUserId and o.state in (:states) UNION select top.* from t_order_parking top where top.id not in(select topreserve.id from t_order_parking topreserve,t_order_parking topparking where topreserve.id = topparking.reserve_id) and top.mobile_user_id = :mobileUserId and top.state = :state",nativeQuery = true)
    List<OrderParking> findOrderParkingListByMobileUserIdAndStates(@Param("mobileUserId") Integer mobileUserId, @Param(value = "states") List<Integer> states, @Param("state") Integer state);

    /**
     * 查询某个车牌的未完成订单
     * @param plateId
     * @param states
     * @return
     */
    @Query(value = "select count(o.id) + (select count(top.id) from t_order_parking top where top.id not in(select topreserve.id from t_order_parking topreserve,t_order_parking topparking where topreserve.id = topparking.reserve_id) and top.plate_id = :plateId and top.state = :state) from t_order_parking o  where o.plate_id = :plateId and o.state in (:states)",nativeQuery = true)
    Integer findOrderParkingByPlateIdAndStates(@Param("plateId") Integer plateId, @Param(value = "states") List<Integer> states, @Param("state") Integer state);

    /**
     * 查询某个发布的未完成订单
     * @param publishId
     * @param states
     * @return
     */
    @Query(value = "select count(o.id) + (select count(top.id) from t_order_parking top where top.id not in(select topreserve.id from t_order_parking topreserve,t_order_parking topparking where topreserve.id = topparking.reserve_id) and top.reservation_id in(select rn.id from t_share s,t_reservation rn where s.id=rn.share_id and s.publish_id=:publishId) and top.state = :state) from t_order_parking o  where o.reservation_id in(select rn.id from t_share s,t_reservation rn where s.id=rn.share_id and s.publish_id=:publishId) and o.state in (:states)",nativeQuery = true)
    Integer findOrderParkingByPublishIdAndStates(@Param("publishId") Integer publishId, @Param(value = "states") List<Integer> states, @Param("state") Integer state);


    /**
     * 更新商户订单号
     * @param id
     * @param tradeNo
     */
    @Modifying
    @Query("update OrderParking set tradeNo = :tradeNo where id = :id")
    void updateTradeNoById(@Param("id") Integer id, @Param("tradeNo") String tradeNo);

    /**
     * 支付之后改变订单状态
     * @param realFee 实付金额
     * @param tripartiteFee 第三方支付手续费
     * @param payChannel 支付渠道
     * @param platformIncome 平台收入
     * @param ownerIncome 业主收入
     * @param manageIncome 物业收入
     * @param payTime 支付时间
     * @param state 要改变成的状态
     * @param id 订单Id
     */
    @Modifying
    @Query("update OrderParking set discountFee=totalFee-:realFee,realFee=:realFee,tripartiteFee=:tripartiteFee,payChannel=:payChannel,platformIncome=:platformIncome,ownerIncome=:ownerIncome,manageIncome=:manageIncome,payTime=:payTime,state=:state where id = :id")
    void payReserve(@Param("realFee") BigDecimal realFee, @Param("tripartiteFee") BigDecimal tripartiteFee, @Param("payChannel") Integer payChannel, @Param("platformIncome") BigDecimal platformIncome, @Param("ownerIncome") BigDecimal ownerIncome, @Param("manageIncome") BigDecimal manageIncome, @Param("payTime") Long payTime, @Param("state") Integer state, @Param("id") Integer id);

    /**
     * 支付之后改变订单状态
     * @param realFee 实付金额
     * @param tripartiteFee 第三方支付手续费
     * @param payChannel 支付渠道
     * @param platformIncome 平台收入
     * @param ownerIncome 业主收入
     * @param manageIncome 物业收入
     * @param payTime 支付时间
     * @param state 要改变成的状态
     * @param invoiceState 可开票状态
     * @param id 订单Id
     */
    @Modifying
    @Query("update OrderParking set discountFee=totalFee-:realFee,realFee=:realFee,tripartiteFee=:tripartiteFee,payChannel=:payChannel,platformIncome=:platformIncome,ownerIncome=:ownerIncome,manageIncome=:manageIncome,payTime=:payTime,invoiceState=:invoiceState,state=:state where id = :id")
    void payParking(@Param("realFee") BigDecimal realFee, @Param("tripartiteFee") BigDecimal tripartiteFee, @Param("payChannel") Integer payChannel, @Param("platformIncome") BigDecimal platformIncome, @Param("ownerIncome") BigDecimal ownerIncome, @Param("manageIncome") BigDecimal manageIncome, @Param("payTime") Long payTime, @Param("invoiceState") Integer invoiceState, @Param("state") Integer state, @Param("id") Integer id);


    /**
     * 更新可开票状态
     * @param id
     * @param invoiceState
     */
    @Modifying
    @Query("update OrderParking set invoiceState = :invoiceState where id = :id")
    void updateInvoiceState(@Param("id") Integer id, @Param("invoiceState") Integer invoiceState);

    /**
     * 根据预约订单查询停车订单
     * @param reserveId
     * @return
     */
    OrderParking findByReserveId(Integer reserveId);

    /**
     * 根据订单号查询订单
     * @param serialNumber
     * @return
     */
    OrderParking findBySerialNumber(String serialNumber);

    /**
     * 根据第三方流水号
     * @param tradeNo
     * @return
     */
    OrderParking findByTradeNo(String tradeNo);

    /**
     * 根据Id改变订单状态
     * @param id
     * @param state
     * @param invoiceState
     */
    @Modifying
    @Query("update OrderParking set cancelTime = :cancelTime,state = :state,invoiceState = :invoiceState where id = :id")
    void updateStateAndCancelTime(@Param("id") Integer id, @Param("state") Integer state, @Param("cancelTime") Long cancelTime, @Param("invoiceState") Integer invoiceState);

    /**
     * 根据停车区id，车牌号id和订单状态查询预约订单，根据找到的预约订单生成一条新的停车订单
     * @param parklotId 停车区id
     * @param plateId 车牌号id
     * @param state 状态
     * @return 预约订单
     */
    List<OrderParking> findByParklotIdAndPlateIdAndStateOrderByIdDesc(Integer parklotId, Integer plateId, Integer state);

    List<OrderParking> findByParklotIdAndPlateIdAndStateIn(Integer parklotId, Integer plateId, List<Integer> states);

    /**
     * 根据状态查询所有订单
     * @param states
     * @return
     */
    @Query("select o from OrderParking o where o.state in(:states)")
    List<OrderParking> findByStates(@Param("states") List<Integer> states);

    /**
     * 根据用户名查询停车订单
     * @param userId 用户id
     * @return 停车订单列表
     */
    OrderParking findTopByMobileUserIdOrderByIdDesc(Integer userId);


    /**
     * 某一时间段内所有实付金额总和
     * @param startTime
     * @param endTime
     * @return
     */
    @Query("select sum(o.realFee)from OrderParking o where o.payTime between ?1 and ?2")
    BigDecimal sumOfRealFeeByPayTime(Long startTime, Long endTime);

    /**
     * 所有实付金额总和
     * @return
     */
    @Query("select sum(o.realFee)from OrderParking o")
    BigDecimal sumOfRealFee();


    /**
     * 某一时间段内所有某个渠道实付金额总和
     * @param startTime
     * @param endTime
     * @param payChannel
     * @return
     */
    @Query("select sum(o.realFee)from OrderParking o where o.payTime between ?1 and ?2 and o.payChannel = ?3")
    BigDecimal sumOfRealFeeByPayTimeAndPayChannel(Long startTime, Long endTime, Integer payChannel);

    /**
     * 所有实付金额总和
     * @param payChannel
     * @return
     */
    @Query("select sum(o.realFee)from OrderParking o where o.payChannel = ?1")
    BigDecimal sumOfRealFeeByPayChannel(Integer payChannel);


    /**
     * 某一时间段内所有可开票金额总和
     * @param startTime
     * @param endTime
     * @return
     */
    @Query("select sum(o.realFee)from OrderParking o where o.payTime between ?1 and ?2 and o.invoiceState is not null")
    BigDecimal sumOfRealFeeByPayTimeAndInvoiceStateIsNotNull(Long startTime, Long endTime);

    /**
     * 某一时间段内所有已开票金额总和
     * @param startTime
     * @param endTime
     * @param invoiceState
     * @return
     */
    @Query("select sum(o.realFee)from OrderParking o where o.payTime between ?1 and ?2 and o.invoiceState = ?3")
    BigDecimal sumOfRealFeeByPayTimeAndInvoiceState(Long startTime, Long endTime, Integer invoiceState);


    /**
     * 所有可开票金额总和
     * @return
     */
    @Query("select sum(o.realFee)from OrderParking o where o.invoiceState is not null")
    BigDecimal sumOfRealFeeByInvoiceStateIsNotNull();

    /**
     * 所有已开票金额总和
     * @param invoiceState
     * @return
     */
    @Query("select sum(o.realFee)from OrderParking o where o.invoiceState = ?1")
    BigDecimal sumOfRealFeeByInvoiceState(Integer invoiceState);

    @Query(value = "select distinct reserve.id from t_order_parking reserve ,t_order_parking parking where reserve.id= parking.reserve_id",nativeQuery = true)
    List<Integer> findReserveIdFinished();

    /**
     * 更新支付渠道
     * @param id
     * @param payChannel
     */
    @Modifying
    @Query("update OrderParking set payChannel = :payChannel where id = :id")
    void updatePayChannel(@Param("id") Integer id, @Param("payChannel") Integer payChannel);


    /**
     * 更新订单编号
     * @param id
     * @param serialNumber
     */
    @Modifying
    @Query("update OrderParking set serialNumber = :serialNumber where id = :id")
    void updateSerialNumber(@Param("id") Integer id, @Param("serialNumber") String serialNumber);

    /**
     * 根据用户ID和状态查询未完成的停车订单
     * @param userId
     * @param states
     * @return
     */
    @Query("select o from OrderParking o where o.mobileUserId=?1 and o.state in ?2 and o.reserveId is not null")
    OrderParking findAllByMobileUserIdParkOrder(Integer userId, List<Integer> states);

    /**
     * 查询用户的所有预约订单ID
     * @param userId
     * @return
     */
    @Query("select o.reserveId from OrderParking o where o.mobileUserId=?1 and o.reserveId is not null")
    List<Integer> findAllByMobileUserIdState(Integer userId);
    /**
     * 根据用户ID和状态查询未完成的预约订单
     * @param userId
     * @param states
     * @return
     */
    @Query("select o from OrderParking o where o.mobileUserId=?1 and o.state in ?2 and o.id not in ?3 and o.reserveId is null ")
    OrderParking findAllByMobileUserIdReserveOrder(Integer userId, List<Integer> states, List<Integer> ids);

    /**
     * 更新分成
     * @param id
     * @param platformIncome
     * @param ownerIncome
     * @param manageIncome
     */
    @Modifying
    @Query("update OrderParking set platformIncome = :platformIncome,ownerIncome = :ownerIncome,manageIncome = :manageIncome,payChannel=:payChannel where id = :id")
    void updateIncomeAndPayChannel(@Param("id") Integer id, @Param("platformIncome") BigDecimal platformIncome, @Param("ownerIncome") BigDecimal ownerIncome, @Param("manageIncome") BigDecimal manageIncome, @Param("payChannel") Integer payChannel);

    /**
     * 更新订单总金额
     * @param id
     * @param totalFee
     */
    @Modifying
    @Query("update OrderParking set totalFee = :totalFee where id = :id")
    void updateTotalFee(@Param("id") Integer id, @Param("totalFee") BigDecimal totalFee);

    @Modifying
    @Query(value = "update OrderParking set thirdPartyNo=:thridPartyNo where id=:id")
    public void updateThirdPartyNo(@Param("id") Integer id, @Param("thridPartyNo") String thridPartyNo);

    @Query("select o.id from OrderParking o where o.serialNumber like ?1")
    public List<Integer> findBySerialNumberLike(String number);

    @Query(value = "select o.state from OrderParking o where o.reserveId=:reserveId")
    public Integer findParkingStateByReserveId(@Param("reserveId") int reserveId);

    @Modifying
    @Query(value = "UPDATE OrderParking SET totalFee=:totalFee, overtimeFee=:overtimeFee, thirdPartyNo=:thridPartyNo, " +
            "realEndTime=:now , overtime=:overtime WHERE id=:orderId")
    public void updateOrderById(@Param("orderId") Integer orderId, @Param("totalFee") BigDecimal totalFee,
                                @Param("overtimeFee") BigDecimal overtimeFee, @Param("thridPartyNo") String thridPartyNo,
                                @Param("overtime") Long overtime, @Param("now") Long now);

    @Query("select o from OrderParking o where o.parklotId=?1 and o.parklocId=?2 and o.state=?3 and o.reserveId is null ORDER BY o.id desc")
    public List<OrderParking> findOrderParkingByReservation(Integer parkLotId, Integer parkLocId, Integer state);

    @Query("select o from OrderParking o where o.parklotId=?1 and o.parklocId=?2 and o.state=?3 and o.reserveId is not null ORDER BY o.id desc")
    public List<OrderParking> findOrderParkingByParklot(Integer parkLotId, Integer parkLocId, Integer state);

    @Query("select o from OrderParking o where o.reserveId=?1  and o.state in ?2")
    public List<OrderParking> findOrderParkingByParklotInfo(Integer parkLotId, List<Integer> states);

    @Query("select o from OrderParking o where o.uniqueId=?1")
    public List<OrderParking> findOrderParkingByUniqueId(String uniqueId);
}

package com.qhieco.apiservice;

import com.qhieco.request.api.ReserveOrderDetailRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.api.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/2/28 16:12
 * <p>
 * 类说明：
 * 订单的service
 */

public interface OrderService {

    /**
     * 使用userID查询用户正在使用中的订单（状态为1300表示已预留（未支付预约费），1301表示已预约，1302表示已停车，1303表示已离开未支付）
     *
     * @param userId 用户id
     * @return
     */
    OrderUsingRepData queryOrderUsingByUserId(Integer userId);

    /**
     * 查询账单列表
     *
     * @param userId
     * @param pageNum
     * @param type
     * @param date
     * @return
     */
    List<BillRepData> queryBillListByUserId(Integer userId, int pageNum, String type, String date);

    /**
     * 通过serialNumber查询账单详情
     *
     * @param serialNumber
     * @param type
     * @param userId
     * @return
     */
    BillDetailRepData queryBillDetailBySerialNumber(String serialNumber, Integer type, Integer userId);

    /**
     * 查询用户可开发票订单方法
     *
     * @param userId
     * @param pageNum
     * @return
     */
    List<InvoiceOrderRepData> queryInvoiceOrderListByCondition(Integer userId, int pageNum);

    /**
     * 查询预约订单详情
     *
     * @param orderId 订单id
     * @return 预约订单详情
     */
    ReserveOrderDetailRespData queryReserveOrderDetail(Integer orderId);

    /**
     * 查询预约订单详情
     *
     * @param reserveOrderDetailRequest 预约订单详情请求
     * @return 返回结果
     */
    Resp queryReserveOrderDetail(ReserveOrderDetailRequest reserveOrderDetailRequest);

    /**
     * 查询预约列表
     *
     * @param userId
     * @return
     */
    List<ReserveOrderRespData> queryReserveList(Integer userId, Integer pageNum);

    /**
     * 查询订单列表
     *
     * @param userId
     * @param pageNum
     * @return
     */
    List<OrderManageRespData> queryOrderManageList(Integer userId, Integer pageNum);

    /**
     * 查询订单管理详情
     *
     * @param userId
     * @param orderId
     * @return
     */
    OrderManageDetailRespData queryOrderManageDetail(Integer userId, Integer orderId);

    /**
     * 对未确认订单进行超时处理
     *
     * @param orderId
     * @param shareId
     */
    void updateUnconfirmedOrderTimeout(int orderId, int shareId);

    /**
     * 对已预约未进场订单进行超时处理
     *
     * @param orderId
     * @param shareId
     * @param ownerIncome
     * @param manageIncome
     */
    void updateReservedOrderTimeout(int orderId, int shareId, BigDecimal ownerIncome, BigDecimal manageIncome);

    /**
     * 对退款订单进行处理
     *
     * @param serialNumber
     * @param state
     */
    void updateOrderRefundAndOrderTotal(String serialNumber, int state);

}

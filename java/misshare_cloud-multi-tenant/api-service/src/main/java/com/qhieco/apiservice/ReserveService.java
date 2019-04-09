package com.qhieco.apiservice;

import com.qhieco.request.api.*;
import com.qhieco.response.Resp;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 11:36
 * <p>
 * 类说明：
 * 预约service
 */
public interface ReserveService {

    /**
     * 预约车位
     * @param confirmRequest 确认预约接口
     * @return
     */
    Resp confirm(ConfirmRequest confirmRequest);

    /**
     * 取消预约
     * @param orderIdRequest 取消预约接口
     * @return
     */
    Resp cancel(OrderIdRequest orderIdRequest);


    /**
     * 保存选中时间
     * @param saveSelectRequest
     * @return
     */
    Resp saveSelect(SaveSelectRequest saveSelectRequest);


    /**
     * 保存停车订单
     * @param orderParkingRequest
     * @return
     */
    Resp saveOrderParking(OrderParkingRequest orderParkingRequest);



}

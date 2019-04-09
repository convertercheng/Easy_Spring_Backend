package com.qhieco.webservice;

import com.qhieco.commonentity.OrderParking;
import com.qhieco.request.web.BindFeeRuleRequest;
import com.qhieco.request.web.FeeRequest;
import com.qhieco.request.web.FeeRuleAddParkingRequest;
import com.qhieco.request.web.FeeRuleAddReserveRequest;
import com.qhieco.response.Resp;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/27 下午4:15
 * <p>
 * 类说明：
 *     费用规则的Service
 */

public interface FeeRuleService {

    /**
     * 添加停车费用规则
     * @param feeRuleAddParkingRequest 停车费用规则添加请求
     * @return 添加结果
     */
    Resp addParkingFee(FeeRuleAddParkingRequest feeRuleAddParkingRequest);

    /**
     * 添加预约费用规则
     * @param feeRuleAddReserveRequest 预约费用规则添加请求
     * @return 添加结果
     */
    Resp addReserveFee(FeeRuleAddReserveRequest feeRuleAddReserveRequest);

    /**
     * 绑定费用规则
     * @param bindFeeRuleRequest 绑定费用规则请求
     * @return 绑定结果
     */
    Resp bindFeeRule(BindFeeRuleRequest bindFeeRuleRequest);

    /**
     * 查询停车费用规则
     * @param request　费用分页查询请求
     * @return Resp
     */
    Resp queryParkingFee(FeeRequest request);

    /**
     * 查询预约费用规则
     * @param request 费用分页查询请求
     * @return Resp
     */
    Resp queryReserveFee(FeeRequest request);

    void calculateFee(OrderParking orderParking);
}
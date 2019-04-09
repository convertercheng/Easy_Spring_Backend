package com.qhieco.webservice;

import com.qhieco.request.web.FinanceRequest;
import com.qhieco.response.Resp;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/29 下午8:04
 * <p>
 * 类说明：
 *     财务
 */
public interface FinanceService {

    /**
     * 财务总流量
     * @param financeRequest
     * @return
     */
    Resp total(FinanceRequest financeRequest);


    /**
     * 手续费统计
     * @param financeRequest
     * @return
     */
    Resp tripartiteFee(FinanceRequest financeRequest);


    /**
     * 发票统计
     * @param financeRequest
     * @return
     */
    Resp invoiceFee(FinanceRequest financeRequest);


    /**
     * 提现统计
     * @param financeRequest
     * @return
     */
    Resp withdrawFee(FinanceRequest financeRequest);

    /**
     * 提现统计
     * @param financeRequest
     * @return
     */
    Resp couponFee(FinanceRequest financeRequest);





}

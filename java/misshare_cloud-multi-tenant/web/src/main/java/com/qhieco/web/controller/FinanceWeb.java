package com.qhieco.web.controller;

import com.qhieco.request.web.FinanceRequest;
import com.qhieco.response.Resp;
import com.qhieco.webservice.FinanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/29 下午7:51
 * <p>
 * 类说明：
 *     财务web
 */
@RestController
@RequestMapping("/finance")
@Slf4j
public class FinanceWeb {

    @Autowired
    FinanceService financeService;

    @PostMapping(value = "/total")
    public Resp total(FinanceRequest financeRequest) {
        return financeService.total(financeRequest);
    }


    @PostMapping(value = "/tripartiteFee")
    public Resp tripartiteFee(FinanceRequest financeRequest) {
        return financeService.tripartiteFee(financeRequest);
    }

    @PostMapping(value = "/invoiceFee")
    public Resp invoiceFee(FinanceRequest financeRequest) {
        return financeService.invoiceFee(financeRequest);
    }

    @PostMapping(value = "/withdrawFee")
    public Resp withdrawFee(FinanceRequest financeRequest) {
        return financeService.withdrawFee(financeRequest);
    }


    @RequestMapping(value = "/couponFee")
    public Resp couponFee(FinanceRequest financeRequest) {
        return financeService.couponFee(financeRequest);
    }

}

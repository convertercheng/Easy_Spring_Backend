package com.qhieco.apiwrite.web;

import com.alipay.api.AlipayApiException;
import com.qhieco.apiservice.PayService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.constant.Status;
import com.qhieco.request.api.ParkingInfoRequest;
import com.qhieco.request.api.PayRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.CusAccessObjectUtil;
import com.qhieco.util.ParamCheck;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/5 14:29
 * <p>
 * 类说明：
 * 支付接口层
 */

@RestController
@RequestMapping
@Slf4j
public class PayWeb {


    @Autowired
    private PayService payService;


    @PostMapping("/reserve/pay")
    public Resp reservePay(@RequestBody PayRequest payRequest,HttpServletRequest request) {
        Resp resp = ParamCheck.check(payRequest,  "order_id","channel","type");
        if (!resp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        payRequest.setSpbillCreateIp(CusAccessObjectUtil.getIpAddress(request));
        return payService.pay(payRequest);
    }

    @PostMapping("/parking/pay")
    public Resp parkingPay(@RequestBody PayRequest payRequest,HttpServletRequest request) {
        log.info("停车费支付参数：" + payRequest);
        Resp resp = ParamCheck.check(payRequest,  "order_id","channel","type");
        if (!resp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        payRequest.setSpbillCreateIp(CusAccessObjectUtil.getIpAddress(request));
        return payService.pay(payRequest);
    }

    @PostMapping("/alipay/callback")
    public void alipayCallback(HttpServletRequest httpServletRequest) throws AlipayApiException{
        payService.alipayCallback(httpServletRequest);
    }

    @PostMapping("/wxpay/callback")
    public void wxpayCallback(HttpServletRequest httpServletRequest) {
        payService.wxpayCallback(httpServletRequest);
    }


    @PostMapping("pay/parkingfee")
    public Resp wxSanPayParkingFee(@RequestBody ParkingInfoRequest parkingInfoRequest,HttpServletRequest request) {
        Resp checkResp = ParamCheck.check(parkingInfoRequest, "unionId", "plateNo", "parklotId", "timestamp");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        log.info("扫码 支付停车费接口  request = " + request);
        String ip = CusAccessObjectUtil.getIpAddress(request);
        Resp resp = payService.wxScanPayParkingFee(parkingInfoRequest.getPlateNo(), parkingInfoRequest.getParklotId(), parkingInfoRequest.getUnionId(),
                ip, parkingInfoRequest.getOpenId());
        log.info("扫码支付停车费用接口相应结束：resp = " + resp);
        return resp;
    }

    @GetMapping(value = "refund")
    public void refund(@RequestParam("tradeNo") String tradeNo, @RequestParam("channel") Integer channel
            , @RequestParam("realFee") BigDecimal realFee,@RequestParam("serialNumber") String serialNumber,@Param("flag") String flag) {
        log.info("tradeNo = {},channel = {}, realFee = {},serialNumber = {}, flag = {}", tradeNo, channel, realFee, serialNumber, flag);
        if ("refund".equals(flag)) {
            payService.refund(tradeNo, channel, realFee, serialNumber);
        }
    }
}

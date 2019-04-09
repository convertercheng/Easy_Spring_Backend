package com.qhieco.apiwrite.web;

import com.qhieco.apiservice.ApplyService;
import com.qhieco.apiservice.exception.ParamException;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.constant.Status;
import com.qhieco.request.api.InvoiceMakeRequest;
import com.qhieco.request.api.WithdrawRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.ParamCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 14:29
 * <p>
 * 类说明：
 * 申请接口层
 */

@RestController
@RequestMapping("/apply")
public class ApplyWeb {


    @Autowired
    private ApplyService applyService;

    @PostMapping("/makeInvoice")
    public Resp makeInvoice(@RequestBody InvoiceMakeRequest invoiceMakeRequest) {
        Resp resp = ParamCheck.check(invoiceMakeRequest,  "user_id","phone","type","title","content");
        if (!resp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(resp, Status.ApiErr.PARAMS_ERROR.getCode());
        }
        boolean isError = (invoiceMakeRequest.getType() == 1) && (null == invoiceMakeRequest.getTaxpayerId() || "".equals(invoiceMakeRequest.getTaxpayerId()));
        if(isError) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return applyService.makeInvoice(invoiceMakeRequest);
    }

    @PostMapping("/withdraw")
    public Resp withdraw(@RequestBody WithdrawRequest withdrawRequest) {
        Resp resp = ParamCheck.check(withdrawRequest,  "user_id");
        if (!resp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return applyService.withdraw(withdrawRequest);
    }

}

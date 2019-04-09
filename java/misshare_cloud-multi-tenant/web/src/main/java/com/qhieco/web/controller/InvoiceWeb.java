package com.qhieco.web.controller;

import com.qhieco.constant.Status;
import com.qhieco.request.web.ApplyParklocRequest;
import com.qhieco.request.web.RedInvoiceRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.ParamCheck;
import com.qhieco.webservice.InvoiceService;
import com.qhieco.webservice.exception.ParamException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/29 下午7:51
 * <p>
 * 类说明：
 *     发票web
 */
@RestController
@RequestMapping("invoice")
@Slf4j
public class InvoiceWeb {

    @Autowired
    private InvoiceService invoiceService;

    /**
     * 开具红字发票
     * @param redInvoiceRequest
     * @return
     */
    @PostMapping("redInvoice")
    public Resp redInvoice(RedInvoiceRequest redInvoiceRequest){
        Resp checkResp = ParamCheck.check(redInvoiceRequest, "applyId", "redReason");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return  invoiceService.redInvoice(redInvoiceRequest);
    }


    /**
     * 剩余可开票状态
     * @return
     */
    @PostMapping("left")
    public Resp redInvoice(){
        return  invoiceService.left();
    }


}

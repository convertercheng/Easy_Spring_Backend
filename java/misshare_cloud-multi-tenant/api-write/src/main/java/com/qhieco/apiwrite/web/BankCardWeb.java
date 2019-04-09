package com.qhieco.apiwrite.web;

import com.qhieco.apiservice.BankCardService;
import com.qhieco.apiservice.exception.ParamException;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.constant.Status;
import com.qhieco.request.api.BankCardAddRequest;
import com.qhieco.request.api.BankCardUnbindRequest;
import com.qhieco.request.api.ParklocAddRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.ParamCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/6 14:29
 * <p>
 * 类说明：
 * 隐含卡接口层
 */

@RestController
@RequestMapping("bankcard")
public class BankCardWeb {

    @Autowired
    BankCardService bankCardService;

    @PostMapping("add")
    public Resp addBankCard(@RequestBody BankCardAddRequest bankCardAddRequest) {
        Resp resp = ParamCheck.check(bankCardAddRequest,  "user_id","bank_number","reserved_phone","bank","type");
        if (!resp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return bankCardService.addBankCard(bankCardAddRequest);
    }

    @PostMapping("getBankName")
    public Resp getBankName(@RequestBody BankCardAddRequest bankCardAddRequest){
        Resp resp = ParamCheck.check(bankCardAddRequest,  "bank_number");
        if (!resp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return bankCardService.getBankName(bankCardAddRequest);
    }

    @PostMapping("unbind")
    public Resp unbind(@RequestBody BankCardUnbindRequest bankCardUnbindRequest) {
        Resp resp = ParamCheck.check(bankCardUnbindRequest, "user_id", "bankcard_id", "timestamp");
        if (!resp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(resp, Status.ApiErr.PARAMS_ERROR.getCode());
        }
        return bankCardService.unbind(bankCardUnbindRequest);
    }

}

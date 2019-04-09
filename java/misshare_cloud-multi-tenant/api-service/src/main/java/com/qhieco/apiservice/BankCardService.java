package com.qhieco.apiservice;

import com.qhieco.request.api.BankCardAddRequest;
import com.qhieco.request.api.BankCardUnbindRequest;
import com.qhieco.response.Resp;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/6 13:39
 * <p>
 * 类说明：
 *     银行卡的Service
 */
public interface BankCardService {

    /**
     * 添加银行卡
     * @param bankCardAddRequest
     * @return
     */
    Resp addBankCard(BankCardAddRequest bankCardAddRequest);

    /**
     * 获取银行信息
     * @param bankCardAddRequest
     * @return
     */
    Resp getBankName(BankCardAddRequest bankCardAddRequest);

    /**
     * 解绑银行卡
     * @param bankCardUnbindRequest 银行卡请求类
     * @return 返回结果
     */
    Resp unbind(BankCardUnbindRequest bankCardUnbindRequest);
}

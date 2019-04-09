package com.qhieco.webservice;

import com.qhieco.request.web.IntegralRequest;
import com.qhieco.request.web.RedInvoiceRequest;
import com.qhieco.response.Resp;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/29 下午7:51
 * <p>
 * 类说明：
 *     发票SERVICE
 */
public interface InvoiceService {

    /**
     * 开具红字发票
     * @param redInvoiceRequest
     * @return Resp
     */
    Resp redInvoice(RedInvoiceRequest redInvoiceRequest);

    /**
     * 查询分金盘剩余发票数量
     * @return Resp
     */
    Resp left();

}

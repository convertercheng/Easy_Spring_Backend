package com.qhieco.apiservice;

import com.qhieco.commonentity.Parkloc;
import com.qhieco.commonentity.Parklot;
import com.qhieco.response.Resp;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018-06-26 18:52
 * <p>
 * 类说明：
 * ${description}
 */
public interface OrderChargingService {

    /**
     * 出场或进场生成停车订单或者结束停车订单
     *
     * @param tag,parklotId,parklocId
     * @return
     */
    public Resp process(Integer tag, Integer parklotId, Integer parklocId) throws Exception;

    Resp getFee();
}

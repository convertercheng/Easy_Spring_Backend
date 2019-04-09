package com.qhieco.webservice;

import com.qhieco.commonentity.iotdevice.Barrier;
import com.qhieco.request.web.BarrierRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.BarrierData;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-9 下午3:30
 * <p>
 * 类说明：
 * ${description}
 */
public interface BarrierService {
    /**
     * 小区绑定的道闸
     * @param estateId
     * @return
     */
    Resp<List<BarrierData>> estateBarrier(Integer estateId);

    /**
     * 道闸编辑新增
     * @param barrier
     * @return
     */
    Resp save(Barrier barrier);

    /**
     * 道闸列表查询
     * @param request
     * @return
     */
    Resp query(BarrierRequest request);

    /**
     * 道闸详情获取
     * @param id
     * @return
     */
    Resp one(Integer id);

    /**
     *　excel列表导出
     * @param request
     * @param cls
     * @param outputStream
     * @return
     * @throws IOException
     */
    Resp excel(BarrierRequest request, Class cls, OutputStream outputStream) throws IOException;

    /**
     * 道闸绑定小区
     * @param barrierIds
     * @param parklotId
     * @return
     */
    Resp bind(Integer[] barrierIds, Integer parklotId);

    /**
     * 道闸解绑小区
     * @param barrierId
     * @return
     */
    Resp unbind(Integer barrierId);

    /**
     * 未绑定道闸查询
     * @param requst
     * @return
     */
    Resp unbindQuery(BarrierRequest requst);
}

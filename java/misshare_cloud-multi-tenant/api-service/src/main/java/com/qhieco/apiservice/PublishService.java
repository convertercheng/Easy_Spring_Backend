package com.qhieco.apiservice;

import com.qhieco.commonentity.OrderParking;
import com.qhieco.request.api.PublishAddRequest;
import com.qhieco.request.api.PublishBatchAddRequest;
import com.qhieco.request.api.PublishCancelRequest;
import com.qhieco.request.api.PublishListAlterRequest;
import com.qhieco.response.Resp;

/**
 * @author 刘江茳 3638324586@qq.com
 * @version 2.0.1 创建时间: 2018/3/7 16:12
 * <p>
 * 类说明：
 * 发布的service
 */

public interface PublishService {

    /**
     * 发布车位
     * @param publishAddRequest
     * @return
     */
    Resp addPublish(PublishAddRequest publishAddRequest);


    /**
     * 批量发布车位
     * @param publishBatchAddRequest
     * @return
     */
    Resp batchAdd(PublishBatchAddRequest publishBatchAddRequest);

    /**
     * 取消发布
     * @param publishCancelRequest
     * @return
     */
    Resp cancel(PublishCancelRequest publishCancelRequest);

    /**
     * 修改发布
     * @param publishListAlterRequest
     * @return
     */
    Resp alter(PublishListAlterRequest publishListAlterRequest);


    /**
     * 处理待取消和待修改的发布
     * @param orderParking
     */
    void dealWithToBePublish(OrderParking orderParking);

}

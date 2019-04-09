package com.qhieco.apiservice;

import com.qhieco.request.api.AllowPublishCountQueryRequest;
import com.qhieco.request.api.LockListRequest;
import com.qhieco.request.api.ParklocAddRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.api.AllowPublishCountRespData;
import com.qhieco.response.data.api.ParklocLockAccessListRespData;
import com.qhieco.response.data.api.ParklocPublishInfoRespData;
import com.qhieco.response.data.api.PublishParklocRespData;
import com.qhieco.time.ParklotIdAdParklocIds;

import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/6 13:39
 * <p>
 * 类说明：
 *     停车位的Service
 */
public interface ParklocService {

    /**
     * 用户申请添加车位
     * @param parklocAddRequest 添加车位
     * @return 返回申请添加车位的结果
     */
    Resp addParkloc(ParklocAddRequest parklocAddRequest);

    List<PublishParklocRespData> queryPublishParklocList(Integer userId, Integer state, Integer pageNum);

    /**
     * 管理员根据条件筛选可发布的车位信息
     * @param allowPublishCountQueryRequest 请求
     * @return 当前可发布的车位信息
     */
    AllowPublishCountRespData queryAllowPublishInfo(AllowPublishCountQueryRequest allowPublishCountQueryRequest);

    List<PublishParklocRespData> queryParklocPublishInfoByNumber(Integer userId, String parklocNumber);

    List<ParklocPublishInfoRespData> queryParklocPublishInfoByTime(Integer userId, Long startTime, Long endTime, int pageNum);

    ParklocLockAccessListRespData queryParklocLockListByUserId(Integer userId);

    void updateParklocAdParklotInfo(ParklotIdAdParklocIds parklotIdAdParklocIds);

    /**
     * 查询车锁列表
     * @param lockListRequest
     * @return
     */
    Resp lockList(LockListRequest lockListRequest);
}

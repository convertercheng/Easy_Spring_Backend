package com.qhieco.apiread.web;

import com.qhieco.apiservice.ParklocService;
import com.qhieco.apiservice.exception.ParamException;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.constant.Status;
import com.qhieco.request.api.*;
import com.qhieco.response.Resp;
import com.qhieco.response.data.api.AllowPublishCountRespData;
import com.qhieco.util.CommonUtil;
import com.qhieco.util.ParamCheck;
import com.qhieco.util.RespUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/13 17:15
 * <p>
 * 类说明：
 * ${说明}
 */
@RestController
@RequestMapping(value = "parkloc")
@Slf4j
public class ParklocWeb {
    @Autowired
    private ParklocService parklocService;

    /**
     * 查询用户车位发布列表
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/publish/query")
    public Resp parklocPublishQuery(@RequestBody PublishParklocQueryRequest request) {
//        if (CommonUtil.isTimeStampInValid(request.getTimestamp())) {
//            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
//        }
//        if (request.getUser_id() == null || request.getPage_num() == null || request.getPage_num() < 0 || request.getState() == null) {
//            throw new QhieException(Status.ApiErr.PARAMS_PUBLISH_PARKLOC_QUERY);
//        }
        log.info(" 查询用户车位发布列表方法参数：" + request);
        return RespUtil.successResp(parklocService.queryPublishParklocList(request.getUser_id(), request.getState(), request.getPage_num()));
    }

    /**
     * 管理员发布车位，根据具体条件查询允许发布的车位信息
     *
     * @param allowPublishCountQueryRequest 根据条件筛选的可发布车位数
     * @return 返回管理员可发布的车位数
     */
    @PostMapping(value = "allow/publish/info")
    public Resp allowPublishCount(@RequestBody AllowPublishCountQueryRequest allowPublishCountQueryRequest) {
        Resp checkResp = ParamCheck.check(allowPublishCountQueryRequest, "user_id", "start_time", "end_time", "mode");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.ApiErr.PARAMS_ERROR.getCode());
        }
        if (CommonUtil.isTimeStampInValid(allowPublishCountQueryRequest.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        AllowPublishCountRespData allowPublishCountRespData = parklocService.queryAllowPublishInfo(allowPublishCountQueryRequest);
        return RespUtil.successResp(allowPublishCountRespData);
    }

    /**
     * 按车位编号查询车位/物业
     *
     * @return
     */
    @PostMapping(value = "/query/byNumber")
    public Resp parklocByNumber(@RequestBody ParklocQueryByNumberRequest request) {
        if (CommonUtil.isTimeStampInValid(request.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        if (request.getUser_id() == null || request.getParkloc_number() == null) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return RespUtil.successResp(parklocService.queryParklocPublishInfoByNumber(request.getUser_id(), request.getParkloc_number()));
    }

    /**
     * 按时段查询车位/物业
     *
     * @return
     */
    @PostMapping(value = "/query/byTime")
    public Resp parklocByTime(@RequestBody ParklocQueryByTimeRequest request) {
        if (CommonUtil.isTimeStampInValid(request.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        if (request.getUser_id() == null || request.getEnd_time() == null || request.getStart_time() == null || request.getPage_num() == null) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return RespUtil.successResp(parklocService.queryParklocPublishInfoByTime(request.getUser_id(), request.getStart_time(),
                request.getEnd_time(), request.getPage_num()));
    }

    /**
     * 车位锁列表
     *
     * @return
     */
    @PostMapping(value = "/lock/list")
    public Resp parklocLockList(@RequestBody UserIdRequest request) {
        if (CommonUtil.isTimeStampInValid(request.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        if (request.getUser_id() == null) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return RespUtil.successResp(parklocService.queryParklocLockListByUserId(request.getUser_id()));
    }


    @PostMapping("/lock/batchList")
    public Resp lockBatchList(@RequestBody LockListRequest lockListRequest) {
        Resp resp = ParamCheck.check(lockListRequest, "userId", "pageNum");
        if ((!resp.getError_code().equals(Status.Common.VALID.getInt())) || lockListRequest.getPageNum() < 0) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return parklocService.lockList(lockListRequest);
    }
}

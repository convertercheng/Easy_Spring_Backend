package com.qhieco.apiread.web;

import com.qhieco.apiservice.ParklotService;
import com.qhieco.apiservice.exception.ParamException;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.constant.Status;
import com.qhieco.request.api.*;
import com.qhieco.response.Resp;
import com.qhieco.response.data.api.ParklotNearByRepData;
import com.qhieco.response.data.api.ParklotUsualRepData;
import com.qhieco.util.CommonUtil;
import com.qhieco.util.ParamCheck;
import com.qhieco.util.RespUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/2/24 13:37
 * <p>
 * 类说明：
 * 停车场controller
 */
@RestController
@RequestMapping("parklot")
@Slf4j
public class ParklotWeb {

    @Autowired
    private ParklotService parklotService;

    /**
     * 获取常用停车场
     */
    @PostMapping(value = "/usual/get")
    public Resp parklotUsual(@RequestBody ParklotUsualRequest request) {
        if (CommonUtil.isTimeStampInValid(request.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        if (request.getUser_id() == null) {
            log.error("获取常用停车场接口， userID为空，调用失败。");
            throw new QhieException(Status.ApiErr.PARAMS_PARKLOT_USUAL);
        }
        ParklotUsualRepData repData = parklotService.queryParklotUsual(request.getUser_id());
        return RespUtil.successResp(repData);
    }

    /**
     * 查询停车场
     */
    @PostMapping(value = "query")
    public Resp parklotQuery(@RequestBody ParklotQueryRequest request) {
        if (CommonUtil.isTimeStampInValid(request.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        if (request.getX() == null || request.getY() == null || StringUtils.isEmpty(request.getKeywords())) {
            log.error("查询停车场方法，参数异常， " + request);
            throw new QhieException(Status.ApiErr.PARAMS_PARKLOT_QUERY);
        }
        return RespUtil.successResp(parklotService.queryParklotListByKeywords(request.getX(), request.getY(), request.getKeywords(), request.getPage_size(), request.getPage_num()));
    }

    /**
     * 查询附近空车位
     */
    @PostMapping(value = "nearby")
    public Resp parklotNearBy(@RequestBody ParklotNearByRequest parklotNearByRequest) {
        Resp checkResp = ParamCheck.check(parklotNearByRequest, "map", "locate", "radius", "timestamp");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.ApiErr.PARAMS_PARKLOT_NEARBY.getCode());
        }
        ParklotNearByRepData parklotNearByRepData = parklotService.queryNearByParklot(parklotNearByRequest);
        return RespUtil.successResp(parklotNearByRepData);
    }

    /**
     * 车场详情
     *
     * @param parklotDetailRequest
     * @return
     */
    @PostMapping("detail")
    public Resp detail(@RequestBody ParklotDetailRequest parklotDetailRequest) {
        if (null == parklotDetailRequest || null == parklotDetailRequest.getParklot_id()) {
            throw new QhieException(Status.ApiErr.PARAMS_PARKLOT_DETAIL);
        }
        return parklotService.detail(parklotDetailRequest);
    }

    /**
     * 进入预约页面
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/reserve/enter")
    public Resp reserveEnter(@RequestBody ReserveEnterRequest request) {
        if (CommonUtil.isTimeStampInValid(request.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        if (request.getParklot_id() == null) {
            throw new QhieException(Status.ApiErr.PARAMS_RESERVE_ENTER);
        }
        log.info("进入预约页面 参数：" + request);
        return RespUtil.successResp(parklotService.queryReserveInfo(request.getParklot_id(), request.getUser_id(),
                request.getParkloc_id(), request.getDistrict_id()));
    }

    /**
     * 查询发布车位最小发布时间
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/publish/interval")
    public Resp queryMinPublishInterval(@RequestBody MinPublishIntervalRequest request) {
        if (CommonUtil.isTimeStampInValid(request.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        if (request.getUser_id() == null) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return RespUtil.successResp(parklotService.queryMinPublishIntervalByUserId(request.getUser_id(), request.getParklot_id()));
    }
}

package com.qhieco.apiservice;

import com.qhieco.request.api.ParklotDetailRequest;
import com.qhieco.request.api.ParklotNearByRequest;
import com.qhieco.request.api.ParklotUsualSetRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.api.*;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/2/24 13:39
 * <p>
 * 类说明：
 * 有关停车场的Service
 */
public interface ParklotService {

    /**
     * 查询用户常用停车场
     *
     * @param userId 用户id
     * @return
     */
    ParklotUsualRepData queryParklotUsual(Integer userId);

    /**
     * 通过关键字模糊查询停车场列表
     *
     * @param x
     * @param y
     * @param keywords
     * @param pageSize
     * @return
     */
    ParklotListRepData queryParklotListByKeywords(Double x, Double y, String keywords, Integer pageSize, Integer pageNum);

    /**
     * 给用户绑定常用停车场
     *
     * @param parklotUsualSetRequest 设置常用停车场的请求参数
     * @return 返回请求结果
     */
    Resp setUsual(ParklotUsualSetRequest parklotUsualSetRequest);

    /**
     * 停车场详情
     *
     * @param parklotDetailRequest 停车场详情的请求参数
     * @return 返回请求结果
     */
    Resp detail(ParklotDetailRequest parklotDetailRequest);

    /**
     * 该方法返回查询附近空车位的结果
     *
     * @param parklotNearByRequest 查询附近空车位的请求
     * @return 返回ParklotNearByRepData
     */
    ParklotNearByRepData queryNearByParklot(ParklotNearByRequest parklotNearByRequest);

    /**
     * 查询预约页面展示信息
     *
     * @param parklotId 停车区id
     * @return 返回信息
     */
    ReserveEnterRepData queryReserveInfo(Integer parklotId, Integer userId, Integer parklocId, Integer parklotDistrictId);

    MinPublishIntervalRespData queryMinPublishIntervalByUserId(Integer userId, Integer parklotId);
}

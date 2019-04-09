package com.qhieco.apiservice;

import com.qhieco.request.api.PlateAddRequest;
import com.qhieco.request.api.PlateDelRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.api.PlateRepData;

import java.util.List;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/2/6 下午1:47
 * <p>
 * 车牌号Service
 */
public interface PlateService {

    /**
     * 查询车牌号列表
     *
     * @return 返回用户车牌号列表
     */
    List<PlateRepData> queryPlateListByUserId(Integer userId, Integer pageNum);

    /**
     * 添加车牌号
     *
     * @param plateAddRequest 车牌号请求
     * @return Resp
     */
    Resp addPlate(PlateAddRequest plateAddRequest);

    /**
     * 删除车牌号
     *
     * @param plateDelRequest 车牌号删除请求
     * @return Resp
     */
    Resp delPlate(PlateDelRequest plateDelRequest);
}

package com.qhieco.apiservice;

import com.qhieco.request.api.AreaGetRequest;
import com.qhieco.response.data.api.AreaGetRepData;

import java.util.List;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/6 下午9:35
 * <p>
 *     地区Service
 *
 */

public interface AreaService {

    /**
     * 获取支持的地区
     * @param areaGetRequest 获取地区请求类
     * @return 返回数据
     */
    List<AreaGetRepData> getSupportAreas(AreaGetRequest areaGetRequest);
}

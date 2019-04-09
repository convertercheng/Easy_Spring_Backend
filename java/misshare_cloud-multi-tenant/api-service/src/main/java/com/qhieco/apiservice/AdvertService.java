package com.qhieco.apiservice;

import com.qhieco.response.data.api.AdvertRepData;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/2/24 9:28
 * <p>
 * 类说明：
 * 广告页service
 */
public interface AdvertService {
    /**
     * 通过手机类型查询广告页信息
     *
     * @param phoneType 手机类型
     * @return 广告返回内容
     */
    AdvertRepData queryAdvert(int phoneType);
}

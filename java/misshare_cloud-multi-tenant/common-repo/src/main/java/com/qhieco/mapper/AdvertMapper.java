package com.qhieco.mapper;

import com.qhieco.response.data.api.AdvertRepData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/2/24 9:31
 * <p>
 * 类说明：
 * 广告页mapper
 */
@Mapper
public interface AdvertMapper {
    /**
     * 通过手机类型查询广告页
     *
     * @param phoneType
     * @return
     */
    AdvertRepData queryAdvertInfoByPhoneType(@Param("phoneType") int phoneType);
}

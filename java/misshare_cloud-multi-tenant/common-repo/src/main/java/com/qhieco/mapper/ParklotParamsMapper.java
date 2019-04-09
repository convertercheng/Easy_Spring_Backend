package com.qhieco.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/5/5 16:55
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface ParklotParamsMapper {
    @Select(value = "SELECT qhvalue FROM b_parklot_params WHERE parklot_id=#{parklotId} AND qhkey=#{key} AND state=1 LIMIT 1")
    public String queryParklotParamsValue(@Param("parklotId") Integer parklotId, @Param("key") String key);
}

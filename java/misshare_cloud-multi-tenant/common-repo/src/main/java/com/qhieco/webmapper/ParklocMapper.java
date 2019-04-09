package com.qhieco.webmapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/5/9 17:23
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface ParklocMapper {

    public int countByParklocNumberAdParklotId(@Param("parklocNumbers") List<String> parklocNumbers, @Param("parklotId") Integer parklotId,
                                               @Param("parklocId") Integer parklocId);
}

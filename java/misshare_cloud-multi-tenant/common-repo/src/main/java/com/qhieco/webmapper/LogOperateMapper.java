package com.qhieco.webmapper;

import com.qhieco.response.data.web.LogOperationData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/7 15:12
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface LogOperateMapper {

    public List<LogOperationData> queryLogOperateListByCondition(@Param("logLoginType") int logLoginType,
                                                                 @Param("startPage") int startPage, @Param("pageSize") int pageSize);

    public int queryLogOperateCount(@Param("logLoginType") int logLoginType);

}

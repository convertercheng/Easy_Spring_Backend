package com.qhieco.mapper;

import com.qhieco.commonentity.File;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/6/4 16:46
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface FileMapper {

    List<File> queryFileListByParklotId(@Param("parklotId") Integer parklotId);
}

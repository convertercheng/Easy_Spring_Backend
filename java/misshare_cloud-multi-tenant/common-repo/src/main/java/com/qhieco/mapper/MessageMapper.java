package com.qhieco.mapper;

import com.qhieco.response.data.api.MessageRespData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/14 14:01
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface MessageMapper {

    List<MessageRespData> queryMessageListByUserId(@Param("userId") Integer userId, @Param("type") int type,
                                                   @Param("state") Integer state, @Param("startPage") Integer startPage,
                                                   @Param("pageSize") Integer pageSize);

}

package com.qhieco.mapper;

import com.qhieco.response.data.api.AccessRespData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/13 13:49
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface AccessMapper {

    List<AccessRespData> queryAccessListByOrderId(@Param("orderId") Integer orderId, @Param("state") Integer state);

    List<AccessRespData> queryAccessListByUserId(@Param("userId") int userId, @Param("state") Integer state);

}

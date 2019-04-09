package com.qhieco.mapper;

import com.qhieco.response.data.api.LockTypeData;
import com.qhieco.response.data.api.ParklocLockRespData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/22 9:41
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface LockMapper {

    HashMap queryLockInfoByParklocId(@Param("orderId") Integer orderId, @Param("valid") Integer valid);

    List<ParklocLockRespData> queryParklocLockListByUserId(@Param("userId") Integer userId);

    public LockTypeData queryIdbyParklocId(@Param("parklocId") Integer parklocId);
}

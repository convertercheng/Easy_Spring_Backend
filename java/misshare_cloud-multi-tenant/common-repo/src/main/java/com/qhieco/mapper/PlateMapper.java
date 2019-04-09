package com.qhieco.mapper;

import com.qhieco.response.data.api.PlateRepData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/2/23 10:46
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface PlateMapper {

    List<PlateRepData> queryPlateListByUserId(@Param("userId") Integer userId, @Param("state") Integer state,
                                              @Param("startPage") int startPage, @Param("pageSize") int pageSize);

    HashMap<String, Object> queryPlateInfoByPlateIdAdUserId(@Param("plateId") Integer plateId, @Param("userId") Integer userId, @Param("valid") Integer valid);

    @Select(value = "SELECT bup.`plate_id` plateId FROM b_user_plate bup WHERE bup.mobile_user_id=#{userId} AND bup.`state`=1")
    List<Integer> queryPlateIdsByUserId(@Param("userId") Integer userId);
}

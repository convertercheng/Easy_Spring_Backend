package com.qhieco.mapper;

import com.qhieco.response.data.api.WithdrawRecordRespData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/14 10:44
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface WithdrawMapper {

    List<WithdrawRecordRespData> queryWithdrawRecordListByUserId(@Param("userId") Integer userId, @Param("processingState") Integer processingState,
                                                                 @Param("startPage") Integer startPage, @Param("pageSize") Integer pageSize);
}

package com.qhieco.mapper;

import com.qhieco.commonentity.FeeRuleParking;
import com.qhieco.request.web.FeeRequest;
import com.qhieco.response.data.web.ParkingFeeRuleInfoData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/5/5 14:27
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface FeeRuleParkingMapper {

    public FeeRuleParking queryFeeRuleParkingByParklotId(@Param("parklotId") Integer parklotId);

    int queryCountParkingFeeRuleList(FeeRequest request);

    List<ParkingFeeRuleInfoData> queryParkingFeeRuleList(FeeRequest request);

    ParkingFeeRuleInfoData queryParkingFeeRuleById(@Param("id") Integer id);
}

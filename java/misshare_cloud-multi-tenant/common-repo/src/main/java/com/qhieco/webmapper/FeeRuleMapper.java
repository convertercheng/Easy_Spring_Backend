package com.qhieco.webmapper;

import com.qhieco.request.web.FeeRequest;
import com.qhieco.response.data.web.ParkingFeeRuleInfoData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/6/26 11:48
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface FeeRuleMapper {

    int queryCountParkingFeeRuleList(FeeRequest request);

    List<ParkingFeeRuleInfoData> queryParkingFeeRuleList(FeeRequest request);

    ParkingFeeRuleInfoData queryParkingFeeRuleById(@Param("id") Integer id);

    ParkingFeeRuleInfoData queryParkingFeeRuleByParkLotId(@Param("id") Integer id);

}

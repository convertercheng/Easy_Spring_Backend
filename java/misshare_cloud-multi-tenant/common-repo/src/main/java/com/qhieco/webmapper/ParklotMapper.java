package com.qhieco.webmapper;

import com.qhieco.commonentity.FeeRuleParking;
import com.qhieco.commonentity.FeeRuleReserve;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/2 15:30
 * <p>
 * 类说明：
 * ${description}
 */
@Mapper
public interface ParklotMapper {

    /**
     * 查询预约费用规则
     * @param parklotId
     * @param state
     * @return
     */
    List<FeeRuleReserve> queryParklotReserveFeeRule(@Param("parklotId") Integer parklotId,
                                                    @Param("state") Integer state);

    /**
     * 查询停车费用规则
     * @param parklotId
     * @param state
     * @return
     */
    List<FeeRuleParking> queryParklotFeeRuleParking(@Param("parklotId") Integer parklotId,
                                                    @Param("state") Integer state);

    public int queryParklotLnglatDuplicateByCondition(@Param("parklotId") Integer parklotId, @Param("lng") Double lng, @Param("lat") Double lat);
}


package com.qhieco.webmapper;

import com.qhieco.response.data.web.PrizeEntityDetailData;
import com.qhieco.response.data.web.PrizeIdAdNameInfoData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.HashMap;
import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/6/12 17:16
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface PrizeMapper {

    public PrizeEntityDetailData queryPrizeById(@Param("id") Integer id);

    public List<PrizeEntityDetailData> queryPrizeListByCondition(HashMap params);

    public int queryCountPrizeListByCondition(HashMap params);

    public List<PrizeIdAdNameInfoData> queryValidPrizeAll(@Param("startTime") Long startTime, @Param("endTime") Long endTime);

    /**
     * 查询此奖品是否在有效期内
     *
     * @param prizeId
     * @return
     */
    public int countValidPrizeById(@Param("prizeId") Integer prizeId);

    /**
     * 查询此奖品是否有用户领取了但是尚未过期
     *
     * @param prizeId
     * @return
     */
    public int countValidReceivePrize(@Param("prizeId") Integer prizeId);

    @Update({"UPDATE t_prize SET state=#{state}, modify_time=UNIX_TIMESTAMP(NOW())*1000 WHERE id=#{id}"})
    public void updatePrizeStateById(@Param("id") Integer id, @Param("state") Integer state);

    /**
     * 奖品是否是未过期且冻结状态
     *
     * @param prizeId
     * @return
     */
    public Integer queryUnexpiredPrizeState(@Param("prizeId") Integer prizeId);

    public int countRepeatPrizeName(@Param("prizeId") Integer prizeId, @Param("prizeName") String prizeName);
}

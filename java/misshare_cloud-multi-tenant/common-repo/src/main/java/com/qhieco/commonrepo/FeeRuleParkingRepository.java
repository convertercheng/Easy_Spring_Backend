package com.qhieco.commonrepo;

import com.qhieco.commonentity.FeeRuleParking;
import com.qhieco.commonentity.FeeRuleReserve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/26 10:34
 * <p>
 * 类说明：
 *       停车收费规则数据库交互层
 */

public interface FeeRuleParkingRepository extends JpaRepository<FeeRuleParking, Integer> ,JpaSpecificationExecutor<FeeRuleParking>{

    /**
     * 根据基础停车收费规则ID修改停车规则状态
     * @param id
     * @param state
     */
    @Modifying
    @Query("update FeeRuleParking set state = ?2,updateTime = ?3 where feeRuleParkingBaseId = ?1")
    void updateStateAndUpdateTimeByFeeRuleParkingBaseId(Integer id, Integer state, Long updateTime);

}

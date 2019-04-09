package com.qhieco.commonrepo;

import com.qhieco.commonentity.relational.ParklotFeeRuleParkingB;
import com.qhieco.commonentity.relational.ParklotFeeRuleReserveB;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/2/27 下午10:26
 * <p>
 * 类说明：
 *     ParklotFeeRuleParkingBRepository
 */

public interface ParklotFeeRuleParkingBRepository extends JpaRepository<ParklotFeeRuleParkingB, Integer> {

    /**
     * 根据parklot查询停车费用规则
     * @param parklotId 停车场id
     * @param state 状态
     * @return 停车场收费规则
     */
    ParklotFeeRuleParkingB findByParklotIdAndState(Integer parklotId, Integer state);

}

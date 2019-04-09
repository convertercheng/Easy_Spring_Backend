package com.qhieco.commonrepo;

import com.qhieco.commonentity.relational.ParklotFeeRuleReserveB;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/2/27 下午10:26
 * <p>
 * 类说明：
 *     ParklotFeeRuleReserveBRepository
 */

public interface ParklotFeeRuleReserveBRepository extends JpaRepository<ParklotFeeRuleReserveB, Integer> {

    /**
     * 根据parklot查询费用规则
     * @param parklotId 停车场id
     * @param state 状态
     * @return 停车场收费规则list
     */
    ParklotFeeRuleReserveB findByParklotIdAndState(int parklotId, Integer state);

}

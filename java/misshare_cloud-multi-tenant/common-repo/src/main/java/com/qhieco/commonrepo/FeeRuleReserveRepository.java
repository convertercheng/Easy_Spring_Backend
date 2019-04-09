package com.qhieco.commonrepo;

import com.qhieco.commonentity.FeeRuleReserve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/26 10:34
 * <p>
 * 类说明：
 *       收费规则数据库交互层
 */

public interface FeeRuleReserveRepository extends JpaRepository<FeeRuleReserve, Integer>,JpaSpecificationExecutor<FeeRuleReserve> {

    /**
     * 通过名称查询收费规则
     * @param name 收费规则名称
     * @return 收费规则
     */
    List<FeeRuleReserve> findByName(String name);
}

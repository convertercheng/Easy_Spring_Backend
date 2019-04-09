package com.qhieco.commonrepo;

import com.qhieco.commonentity.FeeRuleParkingBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/26 10:34
 * <p>
 * 类说明：
 *       停车收费规则数据库交互层
 */

public interface FeeRuleParkingBaseRepository extends JpaRepository<FeeRuleParkingBase, Integer> ,JpaSpecificationExecutor<FeeRuleParkingBase>{

    /**
     * 通过名称查询收费规则
     * @param name 收费规则名称
     * @return 收费规则
     */
    List<FeeRuleParkingBase> findByName(String name);


    /**
     * 根据名称查询除了本身之外的收费规则
     * @param name
     * @param id
     * @return
     */
    @Query("select fr from  FeeRuleParkingBase fr where fr.name=?1 and fr.id <> ?2")
    List<FeeRuleParkingBase> findByNameExceptMyself(String name, Integer id);




    @Query("select fr from FeeRuleParkingBase fr,ParklotFeeRuleParkingB fb where fb.parklotId = ?1 and fr.id = fb.feeRuleId")
    List<FeeRuleParkingBase> findByParklot(Integer parklotId);
}

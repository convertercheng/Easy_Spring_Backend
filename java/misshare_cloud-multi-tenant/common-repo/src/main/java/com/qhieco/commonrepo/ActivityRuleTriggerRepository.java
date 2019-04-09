package com.qhieco.commonrepo;

import com.qhieco.commonentity.ActivityRuleTrigger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/6/12 19:51
 * <p>
 * 类说明：
 * ${说明}
 */
public interface ActivityRuleTriggerRepository extends JpaRepository<ActivityRuleTrigger, Integer>, JpaSpecificationExecutor<ActivityRuleTrigger> {
}

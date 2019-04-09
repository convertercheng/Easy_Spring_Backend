package com.qhieco.commonrepo;

import com.qhieco.commonentity.relational.ActivityTagB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/6/12 19:51
 * <p>
 * 类说明：
 * ${说明}
 */
public interface ActivityTagBRepository extends JpaRepository<ActivityTagB, Integer>, JpaSpecificationExecutor<ActivityTagB> {

    /**
     * 根据活动ID改变状态
     * @param state
     * @param activityId
     */
    @Modifying
    @Query("update ActivityTagB set state = :state where activityId = :activityId")
    void updateStateByActivityId(@Param("state") Integer state, @Param("activityId") Integer activityId);
}

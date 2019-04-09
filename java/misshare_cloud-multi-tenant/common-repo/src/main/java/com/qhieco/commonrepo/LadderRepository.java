package com.qhieco.commonrepo;

import com.qhieco.commonentity.Ladder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/6/12 19:51
 * <p>
 * 类说明：
 * ${说明}
 */
public interface LadderRepository extends JpaRepository<Ladder, Integer>, JpaSpecificationExecutor<Ladder> {

    /**
     * 根据ID列表改变状态
     * @param state
     * @param ids
     */
    @Modifying
    @Query("update Ladder set state = :state where id in(:ids)")
    void updateStateByIds(@Param("state") Integer state, @Param("ids") List<Integer> ids);
}

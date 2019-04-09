package com.qhieco.commonrepo;

import com.qhieco.commonentity.Update;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/26 下午11:11
 * <p>
 * 类说明：
 * UpdateRepository
 */

public interface UpdateRepository extends JpaRepository<Update, Integer> {


    /**
     * 查询所有强制更新不为空的数据
     *
     * @param type
     * @return
     */
    @Query("select u from Update u where u.forceupdate is not null and u.type = :type")
    List<Update> findForceUpdateList(@Param("type") Integer type);


    /**
     * 查询最新的一个版本
     *
     * @param type
     * @return
     */
    @Query(value = "select * from t_update where type=:type ORDER BY id desc LIMIT 1", nativeQuery = true)
    Update findLatestInfoByType(@Param("type") int type);

}

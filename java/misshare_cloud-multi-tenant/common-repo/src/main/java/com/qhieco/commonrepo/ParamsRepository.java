package com.qhieco.commonrepo;

import com.qhieco.commonentity.Params;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 下午2:51
 * <p>
 *    系统参数Dao
 *
 */

public interface ParamsRepository extends JpaRepository<Params,Integer>,JpaSpecificationExecutor<Params> {

    /**
     * 根据key查询系统参数
     * @param key key
     * @param state state
     * @return String
     */
    @Query("select qhValue from Params where qhKey = :key and state = :state")
    String getValue(@Param("key") String key, @Param("state") Integer state);

    @Query("select p from Params p where p.qhKey=?1 and p.state=?2")
    Params findParamsBykey(String qhKey, Integer state);

    @Query("select p from Params p where p.qhKey=?1 and p.state=?2 and p.id<>?3")
    Params findParamsBykey(String qhKey, Integer state, Integer id);

}

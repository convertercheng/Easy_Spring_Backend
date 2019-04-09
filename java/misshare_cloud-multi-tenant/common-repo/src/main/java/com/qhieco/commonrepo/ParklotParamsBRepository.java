package com.qhieco.commonrepo;

import com.qhieco.commonentity.relational.ParklotParamsB;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 下午2:51
 * <p>
 *    停车场对应系统参数Dao
 *
 */
@CacheConfig
public interface ParklotParamsBRepository extends JpaRepository<ParklotParamsB,Integer> {

    /**
     * 根据车场ID和key查询参数值
     * @param parklotId
     * @param key
     * @param state
     * @return String
     */
    //缓存参数配置说明：缓存名称#持续缓存时间#缓存刷新时间，持续时间低于本值后缓存会进行刷新
    @Cacheable(value = "parklotParamB#180#60")
    @Query("select ppb.qhValue from ParklotParamsB ppb where ppb.parklotId = :parklotId and ppb.qhKey = :key and ppb.state = :state")
    String findValueByParklotId(@Param("parklotId") Integer parklotId, @Param("key") String key, @Param("state") Integer state);

    /**
     * 根据车位ID和key查询参数值
     * @param parklocId
     * @param key
     * @param state
     * @return String
     */
    @Query("select ppb.qhValue from ParklotParamsB ppb where ppb.parklotId = (select p.parklotId from Parkloc p where p.id = :parklocId) and ppb.qhKey = :key and ppb.state = :state")
    String findValueByParklocId(@Param("parklocId") Integer parklocId, @Param("key") String key, @Param("state") Integer state);

    /**
     * 根据车场id，key值，状态查询车场参数
     * @param parklotId 停车场id
     * @param qhKey qhkey
     * @param state 状态
     * @return 车场参数
     */
    ParklotParamsB findByParklotIdAndQhKeyAndState(Integer parklotId, String qhKey, Integer state);

}

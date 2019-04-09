package com.qhieco.commonrepo;

import com.qhieco.commonentity.IntegralPermissionsLevel;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/5/24 14:21
 * <p>
 * 类说明：
 * ${说明}
 */
public interface IntegralPermissionsLevelRepository extends JpaRepository<IntegralPermissionsLevel, Integer>, JpaSpecificationExecutor<IntegralPermissionsLevel> {

    @Query(value = "select i from IntegralPermissionsLevel i where i.state=:state order by i.integralPermissionsStart asc ")
    List<IntegralPermissionsLevel> findByState(@Param("state") Integer state);

    /**
     * 根据用户积分查询对应的预约费系数
     *
     * @param integral
     * @return
     */
    @Query(value = "select i.integralPermissionsCoefficient from IntegralPermissionsLevel i where i.state=1 " +
            "and :integral between i.integralPermissionsStart and i.integralPermissionsEnd")
    public Double queryReserveCoefficientByIntegral(@Param("integral") Integer integral);

    /**
     * 修改积分项内容
     * @param id
     * @param integralPluses
     * @param updateTime
     */
    @Modifying
    @Query("update IntegralPermissionsLevel set integralPermissionsCoefficient=?2,updateTime=?3 where id=?1")
    void updateIntegralPermissionsLevel(Integer id, Double integralPluses, Long updateTime);

}

package com.qhieco.commonrepo;
import com.qhieco.commonentity.Integral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/5/24 10:28
 * <p>
 * 类说明：
 * ${description}
 */
public interface IntegralRepository extends JpaRepository<Integral, Integer>,JpaSpecificationExecutor<Integral> {

    /**
     * 根据类型查询加分或者减分项
     * @param integralType
     * @param state
     * @return
     */
    List<Integral> findByIntegralTypeAndState(Integer integralType, Integer state);

    /**
     * 根据code查询加分或者减分项
     * @param integralCode
     * @param state
     * @return
     */
    List<Integral> findByIntegralCodeAndState(String integralCode, Integer state);

    /**
     * 根据类型编码状态查询加分或减分项
     * @param integralCode
     * @param integralType
     * @param state
     * @return
     */
    Integer findByIntegralCodeAndIntegralTypeAndState(String integralCode, Integer integralType, Integer state);

    /**
     * 修改积分项内容
     * @param id
     * @param integralPluses
     * @param updateTime
     */
    @Modifying
    @Query("update Integral set integralPluses=?2,updateTime=?3 where id=?1")
    void updateIntegral(Integer id, Integer integralPluses, Long updateTime);

}

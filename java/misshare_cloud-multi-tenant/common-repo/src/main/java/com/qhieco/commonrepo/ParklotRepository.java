package com.qhieco.commonrepo;

import com.qhieco.commonentity.Parklot;
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
 *       停车位数据库交互层
 */

public interface ParklotRepository extends JpaRepository<Parklot, Integer> ,JpaSpecificationExecutor<Parklot> {

    /**
     * 根据小区名查找小区id
     * @param name
     * @return
     */
    @Query("select id from Parklot where name like ?1")
    List<Integer> findIdsByNameLike(String name);

    /**
     * 根据车位id查找小区
     * @param parklocId
     * @return
     */
    @Query("select t from Parklot t,Parkloc c where t.id = c.parklotId and c.id = ?1")
    Parklot findByParkloc(Integer parklocId);

    /**
     * 根据用户id查询停车场id
     * @param userId 用户id
     * @return 返回停车场id
     */
    @Query("select id from Parklot where mobileUserId = ?1")
    List<Integer> findIdsByMobileUserId(Integer userId);

    /**
     * 根据Ids查询车场
     * @param ids
     * @return
     */
    @Query("select p from Parklot p where p.id in(:ids)")
    List<Parklot> findByIds(@Param("ids") List<Integer> ids);


    Parklot findByExtraParklotId(String extraParklotId);

    @Query(value = "select p.extraParklotId from Parklot p where p.id=:id")
    String findExtraParklotIdById(@Param("id") Integer id);

    /**
     * 根据mobileUserId和state查询车场信息
     * @param mobileUserId
     * @param state
     * @return
     */
    Parklot findByMobileUserIdAndState(Integer mobileUserId, Integer state);

    Parklot findById(Integer id);
}

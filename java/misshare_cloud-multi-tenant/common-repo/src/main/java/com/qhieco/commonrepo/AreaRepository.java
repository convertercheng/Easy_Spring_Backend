package com.qhieco.commonrepo;

import com.qhieco.commonentity.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/6 10:34
 * <p>
 * 类说明：
 *       地区数据库交互层
 */

public interface AreaRepository extends JpaRepository<Area, Integer>, JpaSpecificationExecutor<Area> {

    /**
     * 通过Level查询所有满足的地区列表
     * @param level 地域级别
     * @param parentId 父级地域id
     * @param state 状态，是否开放
     * @return 地域list
     */
    List<Area> findByLevelAndParentIdAndState(Integer level, Integer parentId, Integer state);

    /**
     * 根据名称和级别查询地区
     * @param name 地区名称
     * @param level 地区级别
     * @return
     */
    Area findByNameAndLevel(String name, Integer level);

    /**
     * 根据parentId和state查询地区列表
     * @param parentId 上级地区id
     * @param state 状态
     * @return 地区列表
     */
    List<Area> findByParentIdAndState(Integer parentId, Integer state);

    /**
     * 根据市id查询省Id
     * @param id
     * @return
     */
    @Query("SELECT id FROM Area WHERE parentId IN(  SELECT id FROM Area WHERE parentId IN (SELECT id " +
            "FROM Area WHERE state=1 AND id=:id))")
    List<Integer> findByParentOneId(@Param("id") Integer id);

    /**
     * 根据区ID查询市Id
     * @param id
     * @return
     */
    @Query("SELECT id FROM Area WHERE parentId IN (SELECT id FROM Area WHERE  state=1 AND id=:id)")
    List<Integer> findByParentTwoId(@Param("id") Integer id);
}

package com.qhieco.commonrepo;

import com.qhieco.commonentity.relational.ParklotUsualB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/26 10:34
 * <p>
 * 类说明：
 *       常用停车场数据库交互层
 */

public interface ParklotUsualBRepository extends JpaRepository<ParklotUsualB, Integer> {

    /**
     * 查询常用停车场
     * @param userId
     * @return List<ParklotUsualB>
     */
    List<ParklotUsualB> findByUserId(Integer userId);

    /**
     * 删除常用停车场
     * @param userId 用户Id
     * @param state 状态
     * @return 删除常用停车场
     */
    @Modifying
    @Query("update ParklotUsualB set state = :state where userId = :userId")
    int deleteParklotUsual(@Param("userId") Integer userId, @Param("state") Integer state);

    /**
     * 修改常用停车场
     * @param userId 用户id
     * @param parklotId 车场id
     * @param state 状态
     *
     */
    @Modifying
    @Query("update ParklotUsualB set state = :state ,parklotId = :parklotId where userId = :userId ")
    int shift(@Param("userId") Integer userId, @Param("parklotId") Integer parklotId, @Param("state") Integer state);
}

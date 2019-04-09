package com.qhieco.commonrepo;

import com.qhieco.commonentity.relational.ParklotFileB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/26 10:34
 * <p>
 * 类说明：
 *       停车场文件关联表数据库交互层
 */

public interface ParklotFileBRepository extends JpaRepository<ParklotFileB, Integer> {

    /**
     * 更新车场文件关联表的状态
     * @param parklotId 停车场id
     * @param state 状态
     * @return 影响的条数
     */
    @Modifying
    @Query("update ParklotFileB pf set pf.state = ?3 where pf.parklotId = ?1 and pf.fileId = ?2")
    int updateParklotFileBState(Integer parklotId, Integer fileId, Integer state);

    /**
     * 根据车场id查询文件关系
     * @param parklotId 车场id
     * @param fileId 文件id
     * @param state 状态
     * @return 车场文件关系
     */
    ParklotFileB findByParklotIdAndFileIdAndState(Integer parklotId, Integer fileId, Integer state);

}

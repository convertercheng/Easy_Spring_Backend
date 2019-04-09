package com.qhieco.commonrepo;

import com.qhieco.commonentity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/26 10:34
 * <p>
 * 类说明：
 *       文件表数据库交互层
 */

public interface FileRepository extends JpaRepository<File, Integer> {

    /**
     * 查询停车场关联文件
     * @param parklotId 车场id
     * @return List<File>
     *
     */
    @Query("select f from File f where f.id in (select pf.fileId from ParklotFileB pf where pf.parklotId = :parklotId and pf.state = 1)")
    List<File> findByParklotId(@Param("parklotId") Integer parklotId);

}

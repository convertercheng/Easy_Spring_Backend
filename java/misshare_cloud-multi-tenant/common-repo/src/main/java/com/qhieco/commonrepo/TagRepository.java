package com.qhieco.commonrepo;

import com.qhieco.commonentity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/4/6 下午11:11
 * <p>
 * 类说明：
 *     TagRepository
 */

public interface TagRepository extends JpaRepository<Tag, Integer> ,JpaSpecificationExecutor<Tag> {

    @Modifying
    @Query("update Tag set state = :state where id = :tagId")
    void deleteTag(@Param("tagId") Integer tagId, @Param("state") Integer state);

    @Query("select t from Tag t where t.id in(:ids) and t.state = 1")
    List<Tag> findByIds(@Param("ids") List<Integer> ids);

    @Query("select t from Tag t where  t.state = 1")
    List<Tag> all();

    /**
     * 根据名称查询标签
     * @param name
     * @return
     */
    List<Tag> findByNameAndState(String name, Integer state);


}

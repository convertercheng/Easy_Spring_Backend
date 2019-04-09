package com.qhieco.commonrepo;

import com.qhieco.commonentity.relational.UserTagB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/4/6 下午11:11
 * <p>
 * 类说明：
 *     UserTagBRepository
 */

public interface UserTagBRepository extends JpaRepository<UserTagB, Integer> ,JpaSpecificationExecutor<UserTagB> {


    @Query("select utb.mobileUserId from UserTagB utb where utb.tagId = :id and utb.state = 1")
    List<Integer> findByTagId(@Param("id") Integer id);

}

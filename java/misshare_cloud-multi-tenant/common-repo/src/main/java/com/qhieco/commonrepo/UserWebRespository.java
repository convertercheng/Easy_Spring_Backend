package com.qhieco.commonrepo;

import com.qhieco.commonentity.UserWeb;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 2018/2/18 下午16:23
 * <p>
 * 类说明：
 *    UserWeb数据交互层
 */

public interface UserWebRespository extends JpaRepository<UserWeb, Integer>,JpaSpecificationExecutor<UserWeb> {

    UserWeb findByUsername(String username);

    @Query("update UserWeb u set u.latestLoginTime =?2 where u.id =?1")
    @Modifying
    @Transactional
    void updateLoginTime(Integer id, Long time);

    Page<UserWeb> findByParentId(int parentid, Pageable pageable);  //父账户查找子账户

    Page<UserWeb> findByLevel(int level,Pageable pageable);//根据level字段查找账户类型
}

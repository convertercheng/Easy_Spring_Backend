package com.qhieco.commonrepo;

import com.qhieco.commonentity.UserBItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/7/3 10:23
 * <p>
 * 类说明：
 * UserWeb数据交互层
 */
public interface UserBItemWebRespository extends JpaRepository<UserBItem, Integer>, JpaSpecificationExecutor<UserBItem> {

    UserBItem findByUsername(String username);

    @Query("update UserBItem u set u.latestLoginTime =?2 where u.id =?1")
    @Modifying
    @Transactional
    void updateLoginTime(Integer id, Long time);
}

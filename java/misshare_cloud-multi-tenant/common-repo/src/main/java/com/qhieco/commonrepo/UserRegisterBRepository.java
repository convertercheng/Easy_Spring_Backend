package com.qhieco.commonrepo;

import com.qhieco.commonentity.relational.UserRegisterB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/13 16:54
 * <p>
 * 类说明：
 * ${description}
 */
public interface UserRegisterBRepository extends JpaRepository<UserRegisterB, Integer> {

    /**
     * 查询该用户是否已经参加过活动
     * @param registerId
     * @param Identification
     * @param state
     * @return
     */
    @Query("select u from UserRegisterB u where u.registerId=?1 and u.identification=?2 and u.state=?3")
    UserRegisterB findUserRegisterBByIdentification(Integer registerId, String Identification, Integer state);

    /**
     * 查询该用户是否已经参加过活动
     * @param Identification
     * @param state
     * @return
     */
    @Query("select u from UserRegisterB u where u.identification=?1 and u.state=?2")
    UserRegisterB findUserRegisterBByIdentification(String Identification, Integer state);

}

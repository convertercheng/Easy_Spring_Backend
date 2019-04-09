package com.qhieco.commonrepo;

import com.qhieco.commonentity.relational.UserPlateB;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/26 10:34
 * <p>
 * 类说明：
 *       用户车牌数据库交互层
 */

public interface UserPlateBRepository extends JpaRepository<UserPlateB, Integer> {

    /**
     *  根据用户Id和车牌Id查询有效的用户车牌
     * @param mobileUserId 用户id
     * @param plateId 车牌号id
     * @param state 状态
     * @return List<UserPlateB>
     */
    List<UserPlateB> findByMobileUserIdAndPlateIdAndState(Integer mobileUserId, Integer plateId, Integer state);

}

package com.qhieco.commonrepo;

import com.qhieco.commonentity.BankCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/5 10:34
 * <p>
 * 类说明：
 *       银行卡表数据库交互层
 */

public interface BankCardRepository extends JpaRepository<BankCard, Integer> {

    /**
     * 查询用户绑定的银行卡
     * @param mobileUserId
     * @param state
     * @return
     */
    List<BankCard> findByMobileUserIdAndState(Integer mobileUserId, Integer state);


}

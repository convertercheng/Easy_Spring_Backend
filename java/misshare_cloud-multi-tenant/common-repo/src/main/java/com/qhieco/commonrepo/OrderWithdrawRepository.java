package com.qhieco.commonrepo;

import com.qhieco.commonentity.OrderWithdraw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/26 下午11:11
 * <p>
 * 类说明：
 *     OrderWithdrawRepository
 */

public interface OrderWithdrawRepository extends JpaRepository<OrderWithdraw, Integer> {

    /**
     * 某一时间段内所有退款金额总和
     * @param startTime
     * @param endTime
     * @param state
     * @return
     */
    @Query("select sum(o.balance)from OrderWithdraw o where o.completeTime between ?1 and ?2 and o.state = ?3")
    BigDecimal sumOfBalanceByCompleteTimeAndState(Long startTime, Long endTime, Integer state);


    /**
     * 所有退款金额总和
     * @param state
     * @return
     */
    @Query("select sum(o.balance)from OrderWithdraw o where  o.state = ?1")
    BigDecimal sumOfBalanceByState(Integer state);


    /**
     * 某段时间所有已通过提现的金额和用户类型
     * @param applyTime1
     * @param applyTime2
     * @param status
     * @return
     */
    @Query("select ow.balance, u.type from OrderWithdraw ow, UserMobile u where ow.mobileUserId = u.id and ow.completeTime between ?1 and ?2 and ow.state = ?3")
    List<Object> getBalanceAndType(Long applyTime1, Long applyTime2, Integer status);


    /**
     * 查询所有已通过提现的金额和用户类型
     * @param status
     * @return
     */
    @Query("select ow.balance, u.type from OrderWithdraw ow, UserMobile u where ow.mobileUserId = u.id and ow.state = ?1")
    List<Object> getBalanceAndType(Integer status);

    @Query("select count(ow.id) from OrderWithdraw ow where ow.mobileUserId = ?1 and state in ?2")
    Integer countByUserAndState(Integer userId, Integer state);


}

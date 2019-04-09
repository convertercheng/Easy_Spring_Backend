package com.qhieco.commonrepo;

import com.qhieco.commonentity.BalanceUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/2/26 下午11:11
 * <p>
 * 类说明：
 *     BalanceUserRepository
 */

public interface BalanceUserRepository extends JpaRepository<BalanceUser, Integer> {


    /**
     * 开票
     * @param amount
     * @param mobileUserId
     * @param state
     */
    @Modifying
    @Query("update BalanceUser set balanceInvoice=balanceInvoice - :amount where mobileUserId = :mobileUserId and state=:state")
    void updateBalanceInvoiceByMobileUserId(@Param("amount") BigDecimal amount, @Param("mobileUserId") Integer mobileUserId,
                                            @Param("state") int state);

    /**
     * 根据用户Id和状态查询账户
     * @param mobileUserId
     * @param state
     * @return
     */
    @Query("select b from BalanceUser b where b.mobileUserId=?1 and b.state=?2")
    BalanceUser findByMobileUserIdAndState(Integer mobileUserId, Integer state);


    /**
     * 提现申请冻结金额
     * @param amount
     * @param mobileUserId
     * @param state
     */
    @Modifying
    @Query("update BalanceUser set balanceEarn=0,balanceFrozenWithdraw=balanceFrozenWithdraw+:amount where mobileUserId =:mobileUserId and state=:state")
    void withdraw(@Param("amount") BigDecimal amount, @Param("mobileUserId") Integer mobileUserId,
                  @Param("state") int state);

    /**
     * 更新可开票金额
     * @param realFee
     * @param userId
     */
    @Modifying
    @Query("update BalanceUser set balanceInvoice=balanceInvoice+:realFee where mobileUserId =:userId")
    void updateBalanceInvoice(@Param("realFee") BigDecimal realFee, @Param("userId") Integer userId);


    /**
     * 根据车位Id更新车位所有人的收入
     * @param parklocId
     * @param income
     */
    @Modifying
    @Query("update BalanceUser set balanceEarn=balanceEarn+:income where mobileUserId =(select p.mobileUserId from Parkloc p where p.id=:parklocId)")
    void updateBalanceEarnByParklocId(@Param("parklocId") Integer parklocId, @Param("income") BigDecimal income);

    /**
     * 还原余额和把冻结金额设为0
     * @param amount
     * @param mobileUserId
     * @param state
     */
    @Modifying
    @Query("update BalanceUser set balanceEarn=balanceEarn+:amount,balanceFrozenWithdraw=balanceFrozenWithdraw-:amount where mobileUserId =:mobileUserId and state=:state")
    void withdrawBalanceEarn(@Param("amount") BigDecimal amount, @Param("mobileUserId") Integer mobileUserId,
                             @Param("state") int state);

    /**
     * 通过提现申请 减去冻结的金额
     * @param amount
     * @param mobileUserId
     * @param state
     */
    @Modifying
    @Query("update BalanceUser set balanceFrozenWithdraw=balanceFrozenWithdraw-:amount where mobileUserId =:mobileUserId and state=:state")
    void withdrawBalanceFrozen(@Param("amount") BigDecimal amount, @Param("mobileUserId") Integer mobileUserId,
                               @Param("state") int state);

}

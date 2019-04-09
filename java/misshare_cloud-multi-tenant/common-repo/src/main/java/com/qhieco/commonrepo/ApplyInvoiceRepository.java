package com.qhieco.commonrepo;

import com.qhieco.commonentity.ApplyInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 下午2:51
 * <p>
 *    发票申请Dao
 *
 */

public interface ApplyInvoiceRepository extends JpaRepository<ApplyInvoice ,Integer> {

    /**
     * 发票审批结果
     * @param id
     * @param message
     * @param completeTime
     * @param state
     */
    @Modifying
    @Query("update ApplyInvoice set fileId = :fileId,message = :message,completeTime = :completeTime,state = :state where id = :id")
    void updateResult(@Param("id") Integer id, @Param("fileId") Integer fileId, @Param("message") String message, @Param("completeTime") Long completeTime, @Param("state") Integer state);


    /**
     * 发票冲红
     * @param id
     * @param state
     */
    @Modifying
    @Query("update ApplyInvoice set state = :state where id = :id")
    void updateRed(@Param("id") Integer id, @Param("state") Integer state);



}

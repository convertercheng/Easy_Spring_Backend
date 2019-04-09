package com.qhieco.commonrepo;

import com.qhieco.commonentity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 下午2:51
 * <p>
 *    发票Dao
 *
 */

public interface InvoiceRepository extends JpaRepository<Invoice,Integer> {

    Invoice findByApplyId(Integer applyId);
}

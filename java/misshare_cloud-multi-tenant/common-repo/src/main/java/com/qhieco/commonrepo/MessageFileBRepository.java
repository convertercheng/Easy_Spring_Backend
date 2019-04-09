package com.qhieco.commonrepo;

import com.qhieco.commonentity.relational.MessageFileB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 下午2:51
 * <p>
 *    MessageFileBRepository
 *
 */

public interface MessageFileBRepository extends JpaRepository<MessageFileB, Integer> ,JpaSpecificationExecutor<MessageFileB> {


}

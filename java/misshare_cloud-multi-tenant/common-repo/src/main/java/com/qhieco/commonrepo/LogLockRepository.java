package com.qhieco.commonrepo;

import com.qhieco.commonentity.LogLock;
import com.qhieco.commonentity.OrderParking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/26 10:34
 * <p>
 * 类说明：
 *       停车位数据库交互层
 */

public interface LogLockRepository extends JpaRepository<LogLock, Integer> ,JpaSpecificationExecutor<LogLock> {

    @Query("select count(ll.id) from LogLock ll where ll.lockId = :lockId and ll.rockerState = :rockerState and ll.createTime > :controlTime")
    Integer findLogByLockIdAndRockerStateAndCreateTime(@Param("lockId") Integer lockId, @Param("rockerState") Integer rockerState, @Param("controlTime") Long controlTime);
}

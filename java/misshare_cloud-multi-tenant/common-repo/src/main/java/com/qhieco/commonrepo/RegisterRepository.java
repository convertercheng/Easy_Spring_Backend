package com.qhieco.commonrepo;

import com.qhieco.commonentity.Register;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/12 18:08
 * <p>
 * 类说明：
 * 注册来源标记数据库交互层
 */
public interface RegisterRepository extends JpaRepository<Register, Integer>,JpaSpecificationExecutor<Register> {
}

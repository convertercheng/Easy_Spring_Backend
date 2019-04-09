package com.qhieco.commonrepo;

import com.qhieco.commonentity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 19-1-10 上午9:47
 * <p>
 * 类说明：
 * ${description}
 */
public interface TenantRepository extends JpaRepository<Tenant, Integer> ,JpaSpecificationExecutor<Tenant>{
}

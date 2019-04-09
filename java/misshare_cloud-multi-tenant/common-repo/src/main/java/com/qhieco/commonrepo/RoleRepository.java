package com.qhieco.commonrepo;

import com.qhieco.commonentity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Set;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 2018/2/18 下午16:23
 * <p>
 * 类说明：
 *    UserWeb数据交互层
 */
public interface RoleRepository extends JpaRepository<Role, Integer>, JpaSpecificationExecutor<Role> {
    Role findByName(String name);

    Set<Role> findByIdIn(Integer[] roleIds);

    Page<Role> findByLevel(int level, Specification<Role> roleSpecification, Pageable pageable);
}

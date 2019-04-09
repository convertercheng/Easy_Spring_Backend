package com.qhieco.commonrepo;

import com.qhieco.commonentity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-4-4 下午3:34
 * <p>
 * 类说明：
 * ${description}
 */
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    Set<Permission> findByIdIn(Integer[] ids);

    Permission findByName(String name);

    Permission findByUrl(String url);

    /**
     * 将allocation与得到的level相与 如果得到的与原level相同则可以赋予该权限
     * @param level
     * @return
     */
    @Query(value = "select * from t_permission as p where p.allocation & (:level) = (:level)", nativeQuery = true)
    List<Permission> findByUserLevel(@Param("level")Integer level);
}

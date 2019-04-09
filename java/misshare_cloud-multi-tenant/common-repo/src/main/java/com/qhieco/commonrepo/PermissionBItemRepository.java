package com.qhieco.commonrepo;

import com.qhieco.commonentity.PermissionBItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-4-4 下午3:34
 * <p>
 * 类说明：
 * ${description}
 */
public interface PermissionBItemRepository extends JpaRepository<PermissionBItem, Integer> {

    Set<PermissionBItem> findByIdIn(Integer[] ids);

    PermissionBItem findByName(String name);

    PermissionBItem findByUrl(String url);


}

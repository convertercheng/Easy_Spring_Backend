package com.qhieco.bitemservice;


import com.qhieco.response.data.web.PermissionBItemData;
import java.util.List;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/3/27 19:27
 * <p>
 * 类说明：
 * ${description}
 */
public interface PermissionBItemService {

    List<PermissionBItemData> findByUserId(Integer userId);

}

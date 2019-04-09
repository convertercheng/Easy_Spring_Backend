package com.qhieco.webbitemmapper;

import com.qhieco.response.data.web.PermissionBItemData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/7/3 15:29
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
    public interface PermissionBItemMapper {

    List<PermissionBItemData> findByUserId(Integer userId);

}

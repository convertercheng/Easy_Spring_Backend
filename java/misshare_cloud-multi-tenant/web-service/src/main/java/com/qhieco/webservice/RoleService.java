package com.qhieco.webservice;

import com.qhieco.commonentity.Role;
import com.qhieco.request.web.RoleRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.AbstractPaged;
import okhttp3.internal.http1.Http1Codec;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;


/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-15 上午10:33
 * <p>
 * 类说明：
 * ${description}
 */
public interface RoleService {
    /**
     * 角色excel导出
     * @param name
     * @param startCreateTime
     * @param endCreateTime
     * @param outputStream
     * @return
     * @throws IOException
     */
    Resp excel(Integer level, String name, Long startCreateTime, Long endCreateTime, OutputStream outputStream) throws IOException;

    /**
     * 返回role的list
     * @return resp
     */
    Resp<List<Role>> findAll();

    /**
     * 角色列表查询
     * @param roleRequest
     * @return
     */
    Resp<AbstractPaged<Role>> all(RoleRequest roleRequest);

    /**
     * 角色新增编辑
     * @param role
     * @return
     */
    Resp save(Role role);

    /**
     * 查找角色拥有的权限
     * @param roleId 角色id
     * @return resp<List<Permission>>
     */
    Resp findPermission(Integer roleId);

    /**
     * 查找角色尚未拥有的权限
     * @param roleId 角色id
     * @return resp<List<Permission>>
     */
    Resp noPermission(Integer roleId);

    /**
     * 角色绑定权限
     * @param roleId 角色id
     * @param permissionId 权限id列表
     * @return resp
     */
    Resp bindPermission(Integer roleId, Integer[] permissionId);

    /**
     * 角色解绑权限
     * @param roleId 角色id
     * @param permissionIds 权限id列表
     * @return resp
     */
    Resp unbindPermission(Integer roleId, Integer[] permissionIds);

}

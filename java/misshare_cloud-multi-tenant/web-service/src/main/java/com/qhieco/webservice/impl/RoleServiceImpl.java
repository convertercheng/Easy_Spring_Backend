package com.qhieco.webservice.impl;


import com.qhieco.anotation.AddTenantInfo;
import com.qhieco.anotation.EnableTenantFilter;
import com.qhieco.commonentity.Role;
import com.qhieco.commonentity.UserWeb;
import com.qhieco.commonrepo.PermissionRepository;
import com.qhieco.commonrepo.RoleRepository;
import com.qhieco.constant.Status;
import com.qhieco.request.web.RoleRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.AbstractPaged;
import com.qhieco.util.BeanUtil;
import com.qhieco.util.ExcelUtil;
import com.qhieco.util.PageableUtil;
import com.qhieco.webservice.utils.PropertyCheck;
import com.qhieco.util.RespUtil;
import com.qhieco.webservice.RoleService;
import lombok.val;
import org.apache.http.client.methods.HttpPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
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
@Service
public class RoleServiceImpl implements RoleService {

    private static final String NAME_PROPERTY = "name&tenantId&level";
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PermissionRepository permissionRepository;

    private Specification<Role> allWhere(Integer level, String name, Long startCreateTime, Long endCreateTime) {
        return (root, query, cb) -> {
            val page = new PageableUtil(root,query,cb);
            page.equal("level", level);
            // 角色名称(name)，
            page.like("name", name);
            // 创建时间(startCreateTime/endCreateTime)
            page.between("createTime", startCreateTime, endCreateTime);
            return page.pridect();
        };
    }

    @Override
    @EnableTenantFilter
    public Resp excel(Integer level, String name, Long startCreateTime, Long endCreateTime, OutputStream outputStream) throws IOException {
        List<Role> roleList = roleRepository.findAll(allWhere(level, name, startCreateTime, endCreateTime));
        ExcelUtil<Role> excel = new ExcelUtil<Role>(outputStream, Role.class);
        excel.buildFormat("state", Status.Common::find);
        excel.write(roleList);
        return RespUtil.successResp();
    }

    @Override
    public Resp<List<Role>> findAll(){
        List<Role> roles = roleRepository.findAll();
        return RespUtil.successResp(roles);
    }

    @Override
    @EnableTenantFilter
    @AddTenantInfo
    public Resp<AbstractPaged<Role>> all(RoleRequest roleRequest) {
        Page<Role> page = roleRepository.findAll(
                allWhere(roleRequest.getLevel(), roleRequest.getName(), roleRequest.getStartCreateTime(), roleRequest.getEndCreateTime()),
                PageableUtil.pageable(roleRequest.getSEcho(), roleRequest.getStart(), roleRequest.getLength()));
        List<Role> roleList = page.getContent();
        roleList.forEach(role -> role.setPermissions(null));
        Integer count = (int) page.getTotalElements();
        AbstractPaged<Role> data = AbstractPaged.<Role>builder()
                .sEcho(roleRequest.getSEcho() + 1)
                .iTotalDisplayRecords(count)
                .iTotalRecords(count)
                .dataList(roleList).build();
        return RespUtil.successResp(data);
    }


    @Override
    public Resp save(Role role) {
        try {
            if (!PropertyCheck.uniqueCheck(role, NAME_PROPERTY)){
                return RespUtil.errorResp(
                                Status.WebErr.ROLE_NAME_EXISTS.getCode(),
                                Status.WebErr.ROLE_NAME_EXISTS.getMsg()
                        );
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if(role.getId()!= null){
            val oldData = roleRepository.findOne(role.getId());
            if(oldData!=null) {
                BeanUtil.converJavaBean(oldData, role);
                roleRepository.save(role);
                return RespUtil.successResp();
            }
        }
        role.setCreateTime(System.currentTimeMillis());
        role.setState(Status.Common.VALID.getInt());
        roleRepository.save(role);
        return RespUtil.successResp();
    }

    @Override
    public Resp findPermission(Integer roleId){
        val role = roleRepository.findOne(roleId);
        if(role == null){
            return RespUtil.errorResp(
                    Status.WebErr.ENTITY_NOT_EXISTS.getCode(),
                    Status.WebErr.ENTITY_NOT_EXISTS.getMsg()
            );
        }
        return RespUtil.successResp(role.getPermissions());
    }

    @Override
    public Resp noPermission(Integer roleId){
        val role = roleRepository.findOne(roleId);
        if (role ==  null){
            return RespUtil.errorResp(Status.WebErr.ENTITY_NOT_EXISTS.getCode(),
                    "角色不存在");
        }
        val hasPermission = role.getPermissions();
        val holePermission = permissionRepository.findAll();
        holePermission.removeAll(hasPermission);
        return RespUtil.successResp(holePermission);
    }

    @Transactional
    @Override
    public Resp bindPermission(Integer roleId, Integer[] permissionId){
        val role = roleRepository.findOne(roleId);
        if (role ==  null){
            return RespUtil.errorResp(Status.WebErr.ENTITY_NOT_EXISTS.getCode(),
                    "角色不存在");
        }
        val permissions = permissionRepository.findByIdIn(permissionId);
        if (role.getPermissions()!=null){
            role.getPermissions().addAll(permissions);
        }else {
            role.setPermissions(permissions);
        }
        roleRepository.save(role);
        return RespUtil.successResp();
    }

    @Override
    @Transactional
    public Resp unbindPermission(Integer roleId, Integer[] permissionIds){
        val role = roleRepository.findOne(roleId);
        if (role ==  null){
            return RespUtil.errorResp(Status.WebErr.ENTITY_NOT_EXISTS.getCode(),
                    "用户不存在");
        }
        val permission = permissionRepository.findByIdIn(permissionIds);
        if (role.getPermissions()!=null && permission != null){
            role.getPermissions().removeAll(permission);
        }
        roleRepository.save(role);
        return RespUtil.successResp();
    }



}

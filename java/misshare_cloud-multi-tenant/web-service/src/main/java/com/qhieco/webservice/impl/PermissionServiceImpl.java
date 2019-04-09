package com.qhieco.webservice.impl;

import com.qhieco.commonentity.Permission;
import com.qhieco.commonentity.UserWeb;
import com.qhieco.commonrepo.PermissionRepository;
import com.qhieco.commonrepo.UserWebRespository;
import com.qhieco.constant.Status;
import com.qhieco.request.web.QueryPaged;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.AbstractPaged;
import com.qhieco.util.BeanUtil;
import com.qhieco.util.PageableUtil;
import com.qhieco.webservice.utils.PropertyCheck;
import com.qhieco.util.RespUtil;
import com.qhieco.webservice.PermissionService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import java.util.List;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-4-10 上午10:16
 * <p>
 * 类说明：
 * ${description}
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    private static final String PROPERTY = "name|url";

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    UserWebRespository userWebRespository;

    @Override
    public Resp save(Permission permission) {
        try {
            if (!PropertyCheck.uniqueCheck(permission, PROPERTY)){
                return RespUtil.errorResp(Status.WebErr.PROPERTY_EXISTS.getCode(),
                        "对应属性的数据已存在");
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if(permission.getId()!=null){
            val oldData = permissionRepository.findOne(permission.getId());
            if(oldData!=null){
                BeanUtil.converJavaBean(oldData, permission);
                permissionRepository.save(permission);
                return RespUtil.successResp();
            }
        }
        permission.setCreateTime(System.currentTimeMillis());
        permission.setState(Status.Common.VALID.getInt());
        permissionRepository.save(permission);
        return RespUtil.successResp();
    }

    @Override
    public Resp all(QueryPaged queryPaged){
        Page<Permission> page = permissionRepository.findAll(
                PageableUtil.pageable(queryPaged.getSEcho(), queryPaged.getStart(), queryPaged.getLength()));
        List<Permission> userWebList = page.getContent();
        Integer count = (int) page.getTotalElements();
        AbstractPaged<Permission> data = AbstractPaged.<Permission>builder()
                .sEcho(queryPaged.getSEcho() + 1)
                .iTotalDisplayRecords(count)
                .iTotalRecords(count)
                .dataList(userWebList).build();
        return RespUtil.successResp(data);
    }

    @Override
    public Boolean check(UserWeb admin, String url){
        val userWeb = userWebRespository.findOne(admin.getId());
        val permissions = userWeb.findPermission();
        val matcher = new AntPathMatcher();
        if(permissions.stream().anyMatch(permission -> matcher.match(permission.getUrl(),url))){
            return true;
        }
        val totalPermission = permissionRepository.findAll();
        if (totalPermission.stream().anyMatch(permission -> matcher.match(permission.getUrl(),url))){
            return false;
        }
        return true;
    }
}

package com.qhieco.webservice.impl;

import com.qhieco.anotation.AddTenantInfo;
import com.qhieco.anotation.EnableTenantFilter;
import com.qhieco.commonentity.UserWeb;
import com.qhieco.commonrepo.RoleRepository;
import com.qhieco.commonrepo.UserWebRespository;
import com.qhieco.constant.Status;
import com.qhieco.request.web.QueryPaged;
import com.qhieco.request.web.UserWebRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.AbstractPaged;
import com.qhieco.util.BeanUtil;
import com.qhieco.util.PageableUtil;
import com.qhieco.util.TenantContext;
import com.qhieco.webservice.utils.PropertyCheck;
import com.qhieco.util.RespUtil;
import com.qhieco.webservice.AdminService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-4-9 下午2:19
 * <p>
 * 类说明：
 * ${description}
 */
@Service
public class AdminServiceImpl implements AdminService {
    private static final String NAME_PROPERTY = "username&tenantId&level";
    @Autowired
    UserWebRespository userWebRespository;


    @Autowired
    RoleRepository roleRepository;

    @Override
    public Resp login(HttpServletRequest request) {
        val session = request.getSession();
        val username = request.getParameter("username");
        if (username != null) {
            val user = userWebRespository.findByUsername(username);
            if (user != null) {
                userWebRespository.updateLoginTime(user.getId(), System.currentTimeMillis());
                session.setAttribute("user", user);
                user.setRoles(null);
                return RespUtil.successResp(user);
            }
        }
        return RespUtil.errorResp(Status.WebErr.SYSTEM_ERROR.getCode(), Status.WebErr.SYSTEM_ERROR.getMsg());
    }

    @Override
    public Resp save(UserWeb userWeb) {
        try {
            if (!PropertyCheck.uniqueCheck(userWeb, NAME_PROPERTY)) {
                return RespUtil.errorResp(
                        Status.WebErr.ROLE_NAME_EXISTS.getCode(),
                        "新增的用户属性存在重复"
                );
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if (userWeb.getId() != null) {
            val oldData = userWebRespository.findOne(userWeb.getId());
            if (oldData != null) {
                BeanUtil.converJavaBean(oldData, userWeb);
                userWebRespository.save(userWeb);
                return RespUtil.successResp();
            }
        }
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        UserWeb currentUser = (UserWeb) req.getSession(false).getAttribute("user");
        if(userWeb.getLevel() == 3 && currentUser!=null) {
            userWeb.setParentId(currentUser.getId());
        }
        userWeb.setCreateTime(System.currentTimeMillis());
//        userWeb.setState(Status.Common.VALID.getInt());
        userWeb.setLatestLoginTime(System.currentTimeMillis());
        userWebRespository.save(userWeb);
        return RespUtil.successResp();
    }


    @Override
    @Transactional
    public Resp bindRole(Integer userId, Integer[] roleIds) {
        val user = userWebRespository.findOne(userId);
        if (user == null) {
            return RespUtil.errorResp(Status.WebErr.ENTITY_NOT_EXISTS.getCode(),
                    "用户不存在");
        }
        val roles = roleRepository.findByIdIn(roleIds);
        if (user.getRoles() != null) {
            user.getRoles().addAll(roles);
        } else {
            user.setRoles(roles);
        }
        userWebRespository.save(user);
        return RespUtil.successResp();
    }

    @Override
    @Transactional
    public Resp unbindRole(Integer userId, Integer[] roleIds) {
        val user = userWebRespository.findOne(userId);
        if (user == null) {
            return RespUtil.errorResp(Status.WebErr.ENTITY_NOT_EXISTS.getCode(),
                    "用户不存在");
        }
        val role = roleRepository.findByIdIn(roleIds);
        if (user.getRoles() != null && role != null) {
            user.getRoles().removeAll(role);
        }
        userWebRespository.save(user);
        return RespUtil.successResp();
    }

    @Override
    @EnableTenantFilter
    public Resp all(QueryPaged queryPaged) {
        Page<UserWeb> page = userWebRespository.findAll(
                PageableUtil.pageable(queryPaged.getSEcho(), queryPaged.getStart(), queryPaged.getLength()));
        List<UserWeb> userWebList = page.getContent();
        Integer count = (int) page.getTotalElements();
        userWebList.forEach(UserWeb::genarateRoleName);
        AbstractPaged<UserWeb> data = AbstractPaged.<UserWeb>builder()
                .sEcho(queryPaged.getSEcho() + 1)
                .iTotalDisplayRecords(count)
                .iTotalRecords(count)
                .dataList(userWebList).build();
        return RespUtil.successResp(data);
    }

    @Override
    public Resp findRole(Integer userId) {
        val user = userWebRespository.findOne(userId);
        if (user == null) {
            return RespUtil.errorResp(
                    Status.WebErr.ENTITY_NOT_EXISTS.getCode(),
                    Status.WebErr.ENTITY_NOT_EXISTS.getMsg()
            );
        }
        if (user.getRoles() != null) {
            user.getRoles().forEach(role -> role.setPermissions(null));
        }
        return RespUtil.successResp(user.getRoles());
    }

    @Override
    public Resp findPermission(Integer userId) {
        val user = userWebRespository.findOne(userId);
        if (user == null) {
            return RespUtil.errorResp(
                    Status.WebErr.ENTITY_NOT_EXISTS.getCode(),
                    Status.WebErr.ENTITY_NOT_EXISTS.getMsg()
            );
        }
        return RespUtil.successResp(user.findPermission());
    }

    /**
     * @param request request
     * @return
     * @author ashang
     */
    @Override
    public Resp findAccount(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        UserWeb user = (UserWeb) session.getAttribute("user");
        int level = user.getLevel();
        if (level == 1) {
            return RespUtil.successResp("admin");
        } else if (level == 2) {
            return RespUtil.successResp("daddy");
        } else {
            return RespUtil.successResp("son");
        }
    }


    @Override
    @AddTenantInfo
    public Resp daddyAll(QueryPaged queryPaged) {
        Page<UserWeb> page = userWebRespository.findByLevel(2,
                PageableUtil.pageable(queryPaged.getSEcho(), queryPaged.getStart(), queryPaged.getLength()));
        List<UserWeb> userWebList = page.getContent();
        Integer count = (int) page.getTotalElements();
        userWebList.forEach(UserWeb::genarateRoleName);
        AbstractPaged<UserWeb> data = AbstractPaged.<UserWeb>builder()
                .sEcho(queryPaged.getSEcho() + 1)
                .iTotalDisplayRecords(count)
                .iTotalRecords(count)
                .dataList(userWebList).build();
        return RespUtil.successResp(data);
    }

    @Override
    @AddTenantInfo
    public Resp sonAll(UserWebRequest queryPaged, HttpServletRequest request) {
        UserWeb userWeb = (UserWeb) request.getSession(false).getAttribute("user");
        int id;
        if(Status.UserWebLevel.PLATFORM.getValue().equals(userWeb.getLevel())){
            id = queryPaged.getParentId();
        }else{
            id = userWeb.getId();
        }
        Page<UserWeb> page = userWebRespository.findByParentId(id,
                PageableUtil.pageable(queryPaged.getSEcho(), queryPaged.getStart(), queryPaged.getLength()));
        List<UserWeb> userWebList = page.getContent();
        Integer count = (int) page.getTotalElements();
        userWebList.forEach(UserWeb::genarateRoleName);
        AbstractPaged<UserWeb> data = AbstractPaged.<UserWeb>builder()
                .sEcho(queryPaged.getSEcho() + 1)
                .iTotalDisplayRecords(count)
                .iTotalRecords(count)
                .dataList(userWebList).build();
        return RespUtil.successResp(data);
    }


}

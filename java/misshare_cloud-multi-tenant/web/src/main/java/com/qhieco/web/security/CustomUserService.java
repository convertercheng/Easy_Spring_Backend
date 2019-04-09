package com.qhieco.web.security;

import com.qhieco.commonentity.Permission;
import com.qhieco.commonentity.Role;
import com.qhieco.commonentity.UserWeb;
import com.qhieco.commonrepo.PermissionRepository;
import com.qhieco.commonrepo.UserWebRespository;
import com.qhieco.constant.Status;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-4-4 下午2:44
 * <p>
 * 类说明：
 * ${description}
 */
@Slf4j
@Service
public class CustomUserService implements UserDetailsService { //自定义UserDetailsService 接口

    private static String SUPER_ADMIN = "admin";

    @Autowired
    UserWebRespository userWebRespository;

    @Autowired
    PermissionRepository permissionRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        UserWeb user = userWebRespository.findByUsername(username);
        if (user != null) {
            if (!user.getState().equals(Status.Common.VALID.getInt())) {
                throw new DisabledException("用户已被禁用，无法正常登陆");
            }
            List<GrantedAuthority> authorities = new ArrayList<>();
            List<Permission> permissions = permissionRepository.findByUserLevel(1 << (user.getLevel() - 1));
            for (Permission permission : permissions) {
                if (permission != null && permission.getName() != null) {
                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(permission.getName());
                    //1：此处将权限信息添加到 GrantedAuthority 对象中，在后面进行全权限验证时会使用GrantedAuthority 对象。
                    authorities.add(grantedAuthority);
                }
            }
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
        } else {
            throw new UsernameNotFoundException("admin: " + username + " do not exist!");
        }
    }

}

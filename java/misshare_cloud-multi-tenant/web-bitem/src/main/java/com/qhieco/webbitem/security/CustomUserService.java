package com.qhieco.webbitem.security;

import com.qhieco.bitemservice.PermissionBItemService;
import com.qhieco.commonentity.PermissionBItem;
import com.qhieco.commonentity.UserBItem;
import com.qhieco.commonrepo.PermissionBItemRepository;
import com.qhieco.commonrepo.UserBItemWebRespository;
import com.qhieco.constant.Status;
import com.qhieco.response.data.web.PermissionBItemData;
import lombok.extern.slf4j.Slf4j;
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
    UserBItemWebRespository userBItemWebRespository;

    @Autowired
    PermissionBItemRepository permissionBItemRepository;

    @Autowired
    PermissionBItemRepository permissionBItemDao;

    @Autowired
    PermissionBItemService permissionBItemService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        UserBItem user = userBItemWebRespository.findByUsername(username);
        if (user != null) {
            if(!user.getState().equals(Status.Common.VALID.getInt())){
                throw new DisabledException("用户已被禁用，无法正常登陆");
            }

            List<GrantedAuthority> authorities = new ArrayList <>();
            // 管理员默认获取所有权限
            if(superAdmin(user, authorities)){
                return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
            }
            List<PermissionBItemData> permissions = permissionBItemService.findByUserId(user.getId());

            for (PermissionBItemData permissionData : permissions) {
                if (permissionData != null && permissionData.getName()!=null) {

                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(permissionData.getName());
                    //1：此处将权限信息添加到 GrantedAuthority 对象中，在后面进行全权限验证时会使用GrantedAuthority 对象。
                    authorities.add(grantedAuthority);
                }
            }

//            if(superAdmin(user, authorities)){
//                return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
//            }

//            for(val role:user.getRoles())
//            {
//                permissions.addAll(role.getPermissions());
//            }
//            for (PermissionBItem permission : permissions) {
//                if (permission != null && permission.getName()!=null) {
//
//                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(permission.getName());
//                    //1：此处将权限信息添加到 GrantedAuthority 对象中，在后面进行全权限验证时会使用GrantedAuthority 对象。
//                    authorities.add(grantedAuthority);
//                }
//            }
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
        } else {
            throw new UsernameNotFoundException("admin: " + username + " do not exist!");
        }
    }

    private Boolean superAdmin(UserBItem user, List<GrantedAuthority> authorities){
        if (user.isSuper()){
            List<PermissionBItem> permissions = permissionBItemRepository.findAll();
            permissions.forEach(permission -> {
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(permission.getName());
                //1：此处将权限信息添加到 GrantedAuthority 对象中，在后面进行全权限验证时会使用GrantedAuthority 对象。
                authorities.add(grantedAuthority);
            });
            return true;
        }
        return false;
    }

}

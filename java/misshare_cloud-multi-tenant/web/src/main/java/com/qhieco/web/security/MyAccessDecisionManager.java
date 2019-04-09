package com.qhieco.web.security;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import java.util.Collection;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-4-4 下午4:14
 * <p>
 * 类说明：
 * ${description}
 */
@Service
@Slf4j
public class MyAccessDecisionManager implements AccessDecisionManager {


    /**
     *
     * @param authentication 是释CustomUserService中循环添加到 GrantedAuthority 对象中的权限信息集合.
     * @param object 包含客户端发起的请求的requset信息，可转换为 HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
     * @param configAttributes 为MyInvocationSecurityMetadataSource的getAttributes(Object object)这个方法返回的结果，此方法是为了判定用户请求的url 是否在权限表中，如果在权限表中，则返回给 decide 方法，用来判定用户是否有此权限。如果不在权限表中则放行。
     *   decide 方法是判定是否拥有权限的决策方法，
     * @throws AccessDeniedException
     * @throws InsufficientAuthenticationException
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {

        if(null== configAttributes || configAttributes.size() <= 0) {
            return;
        }
        String needRole;
        for(val c:configAttributes){
            needRole = c.getAttribute();
            //authentication 为在注释1 中循环添加到 GrantedAuthority 对象中的权限信息集合
            for(GrantedAuthority ga : authentication.getAuthorities()) {
                if(needRole.trim().equals(ga.getAuthority())) {
                    return;
                }
            }
        }
        throw new AccessDeniedException("无访问权限");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
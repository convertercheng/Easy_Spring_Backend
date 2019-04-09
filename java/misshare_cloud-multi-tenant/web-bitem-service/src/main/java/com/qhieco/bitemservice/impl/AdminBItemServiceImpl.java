package com.qhieco.bitemservice.impl;

import com.qhieco.commonrepo.RoleRepository;
import com.qhieco.commonrepo.UserBItemWebRespository;
import com.qhieco.constant.Status;
import com.qhieco.response.Resp;
import com.qhieco.bitemservice.AdminBItemService;
import com.qhieco.util.RespUtil;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-4-9 下午2:19
 * <p>
 * 类说明：
 * ${description}
 */
@Service
public class AdminBItemServiceImpl implements AdminBItemService {

    private static final String NAME_PROPERTY = "username";
    @Autowired
    UserBItemWebRespository userBItemWebRespository;


    @Autowired
    RoleRepository roleRepository;

    @Override
    public Resp login(HttpServletRequest request) {
        val session = request.getSession();
        val username = request.getParameter("username");
        if (username != null){
            val user = userBItemWebRespository.findByUsername(username);
            if(user!= null){
                userBItemWebRespository.updateLoginTime(user.getId(),System.currentTimeMillis());
                session.setAttribute("user",user);
                user.setRoles(null);
                return RespUtil.successResp(user);
            }
        }
        return RespUtil.errorResp(Status.BItemErr.SYSTEM_ERROR.getCode(),Status.BItemErr.SYSTEM_ERROR.getMsg());
    }
}

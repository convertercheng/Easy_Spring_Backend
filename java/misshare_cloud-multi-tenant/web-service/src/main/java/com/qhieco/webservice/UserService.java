package com.qhieco.webservice;

import com.qhieco.request.web.UserRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.UserData;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/3/27 19:27
 * <p>
 * 类说明：
 * ${description}
 */
public interface UserService {

    /**
     * 分页显示用户列表信息
     * @param userRequest
     * @return
     */
    Resp pageUser(UserRequest userRequest);

    /**
     * 分页显示用户基础列表信息
     * @param userRequest
     * @return
     */
    Resp pageUserDetailed(UserRequest userRequest);


    /**
     * 导出用户信息列表
     * @param userRequest outputStream  cl
     * @return
     */
    Resp excel(UserRequest userRequest, OutputStream outputStream, Class cl)throws IOException;

    /**
     * 根据Id查询用户详情
     * @param id
     * @return
     */
    Resp findUserOne(Integer id);

    /**
     * 编辑用户姓名和身份证
     * @param userRequest
     * @return
     */
    Resp editUser(UserRequest userRequest);

    /**
     * 解绑微信用户信息
     * @param userId
     * @return
     */
    Resp untieWxUser(Integer userId);
}

package com.qhieco.webbitemmapper;

import com.qhieco.request.web.ParklotRequest;
import com.qhieco.request.web.UserRequest;
import com.qhieco.response.data.web.UserData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/7/3 15:29
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface UserBItemWebMapper {

    /**
     * 更新登录密码
     * @param request
     * @return
     */
    Integer updateLoginPass(UserRequest request);

    /**
     * 分页显示用户列表信息
     *
     * @param userRequest 相对的参数条件
     * @return 用户列表
     */
    List<UserData> pageUser(UserRequest userRequest);

    /**
     * 查询用户总记录数
     *
     * @param userRequest 相对的参数条件
     * @return
     */
    Integer pageUserTotalCount(UserRequest userRequest);

    /**
     * 根据用户名获取用户ID(用户名username存在唯一标识的情况下)
     * @param username
     * @return
     */
    Integer getUserByUserName(String username);


    /**
     * 根据账户ID获取详细
     * @param id
     * @return
     */
    UserData findOne(Integer id);

    /**
     * 根据角色code获取角色ID
     * @param roleName
     * @return
     */
    UserData findRoleByRoleName(String roleCode);

    /**
     * 更新用户角色关系表
     * @param roleId
     * @param userId
     */
    void updateRoleByUser(@Param("roleId") Integer roleId, @Param("userId") Integer userId);
}

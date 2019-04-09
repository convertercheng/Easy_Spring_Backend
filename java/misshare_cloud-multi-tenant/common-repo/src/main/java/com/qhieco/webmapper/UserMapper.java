package com.qhieco.webmapper;

import com.qhieco.commonentity.Tag;
import com.qhieco.request.web.UserRequest;
import com.qhieco.response.data.web.UserData;
import com.qhieco.response.data.web.UserMessageData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/3/31 9:53
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface UserMapper {
    /**
     * 分页显示用户列表信息
     * @param userRequest 相对的参数条件
     * @return 用户列表
     */
    List<UserData> pageUser(UserRequest userRequest);

    /**
     * 分页显示用户基础信息列表
     * @param userRequest 相对的参数条件
     * @return 用户列表
     */
    List<UserData> pageUserDetailed(UserRequest userRequest);

    /**
     * 查询用户总记录数
     * @param userRequest 相对的参数条件
     * @return
     */
    Integer  pageUserTotalCount(UserRequest userRequest);

    /**
     * 查询用户总记录数
     * @param userRequest 相对的参数条件
     * @return
     */
    Integer  pageUserDetailedTotalCount(UserRequest userRequest);


    /**
     * Excel导出用户列表数据
     * @param userRequest
     * @return
     */
    List<UserData> excelUser(UserRequest userRequest);

    /**
     * 根据用户id查询车牌号
     * @param userId
     * @return
     */
    List<String> findUserPlateNumber(Integer userId);


    /**
     * 根据Id查询用户详情
     * @param userRequest
     * @return
     */
    UserData findUserOne(UserRequest userRequest);

    /**
     * 查询符合标签条件的用户总数
     * @param tag
     * @return
     */
    int findTagUserNumber(Tag tag);


    /**
     * 查询符合标签条件的用户Id列表
     * @param tag
     * @return
     */
    List<Integer> findTagUserId(Tag tag);


    /**
     * 查询
     * @param userIds
     * @return
     */
    List<UserMessageData> findMessageData(String userIds);



}


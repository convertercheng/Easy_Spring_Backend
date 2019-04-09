package com.qhieco.apiservice;

import com.qhieco.request.api.UserLoginRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.api.PersonCenterRepData;
import com.qhieco.response.data.api.UserLoginRepData;
import com.qhieco.response.data.api.UserWithdrawAmountRepData;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/2/26 下午9:23
 * <p>
 * 类说明：
 *     UserService 用户服务类
 */

public interface UserService {

    /**
     * 该方法返回用户登录的结果
     * @param userLoginRequest 登录的请求参数
     * @return 返回请求结果
     */
    UserLoginRepData login(UserLoginRequest userLoginRequest);

    /**
     * 根据用户id查询个人中心数据
     * @param userId 用户id
     * @return 个人中心数据
     */
    PersonCenterRepData queryPersonCenterInfo(Integer userId);

    /**
     * 查询用户可提现金额
     * @param userId 用户id
     * @return 返回数据
     */
    UserWithdrawAmountRepData queryUserWithdrawAmountByUserId(Integer userId);

    /**
     * 上传头像接口
     * @param file 文件
     * @param userId 用户id
     * @param timestamp 客户端时间戳
     * @return 上传头像结果
     */
    Resp uploadAvatar(MultipartFile file, Integer userId, String timestamp);

    String findByUserExtraInfo(Map<String, String> wxMap, String state);
}

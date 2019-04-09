package com.qhieco.bitemservice;

import com.qhieco.request.web.UserRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.AbstractPaged;
import com.qhieco.response.data.web.UserData;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/3/27 19:52
 * <p>
 * 类说明：
 * ${description}
 */
public interface UserBItemService {
    public Resp updateLoginPass(UserRequest request);

    public Resp<AbstractPaged<UserData>> pageUser(UserRequest request);

    public Resp<UserData> saveOrUpdate(UserRequest request);

    public Resp<UserData> saveOrUpdateBySubaccount(UserRequest request);

    public Resp<UserData> findUserDetailByUserId(Integer id);
}
package com.qhieco.apiservice;

import com.qhieco.commonentity.Register;
import com.qhieco.commonentity.relational.UserRegisterB;
import com.qhieco.request.api.RegisterRequest;
import com.qhieco.response.Resp;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/12 18:09
 * <p>
 * 类说明：
 * 注册来源标记业务层
 */
public interface RegisterService {

    /**
     * 修改PV或UV的统计量
     * @param registerRequest
     * @return
     */
    Resp edit(RegisterRequest registerRequest);

    /**
     * 保存或修改
     * @param registerRequest
     * @return
     */
    Resp saveUpdateUserRegisterB(RegisterRequest registerRequest);

}

package com.qhieco.webservice;

import com.qhieco.commonentity.Register;
import com.qhieco.commonentity.relational.UserRegisterB;
import com.qhieco.request.web.RegisterRequest;
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
     * 注册来源标记分页列表
     * @param registerRequest
     * @return
     */
    Resp query(RegisterRequest registerRequest);

    /**
     * 新增或编辑注册来源标记
     * @param register
     * @return
     */
    Resp saveUpdate(Register register);

    /**
     * 修改PV或UV的统计量
     * @param id
     * @param type
     * @return
     */
    Resp eidt(Integer id, String type);

    /**
     * 保存或修改
     * @param userRegisterB
     * @return
     */
    Resp saveUpdateUserRegisterB(UserRegisterB userRegisterB);

    /**
     * 根据Id查询注册来源标记
     * @param id
     * @return
     */
    Resp findOne(Integer id);

    /**
     * 编辑注册来源标记
     * @param register
     * @return
     */
    Resp eidtRegister(Register register);

}

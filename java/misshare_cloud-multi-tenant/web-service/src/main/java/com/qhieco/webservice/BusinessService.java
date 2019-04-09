package com.qhieco.webservice;

import com.qhieco.commonentity.Business;
import com.qhieco.request.web.BusinessRequest;
import com.qhieco.response.Resp;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/3 10:51
 * <p>
 * 类说明：
 * 商家信息业务层
 */
public interface BusinessService {

    /**
     * 分页显示合作商家
     * @param businessRequest 请求参数
     * @return
     */
    Resp query(BusinessRequest businessRequest);

    /**
     * 保存或修改商家
     * @param business
     * @return
     */
    Resp saveUpdate(Business business);

    /**
     * 查询商家详情信息
     * @param id
     * @return
     */
    Resp one(Integer id);

    /**
     * 根据ID删除商家信息
     * @param id
     * @return
     */
    Resp del(Integer id);

    /**
     * 查询状态为有效，正常所有商家信息
     * @return
     */
    Resp getAll();

}

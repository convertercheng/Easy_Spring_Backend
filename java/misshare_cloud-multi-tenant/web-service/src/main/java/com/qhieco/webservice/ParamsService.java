package com.qhieco.webservice;

import com.qhieco.commonentity.Params;
import com.qhieco.request.web.ParamsRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.AbstractPaged;
import com.qhieco.response.data.web.ParamsData;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/8 17:10
 * <p>
 * 类说明：
 * ${description}
 */
public interface ParamsService {

    /**
     * 分页显示参数列表信息
     * @param paramsRequest
     * @return
     */
    Resp<AbstractPaged<Params>> query(ParamsRequest paramsRequest);

    /**
     * 增加或修改参数信息
     * @param paramsRequest
     * @return
     */
    Resp saveUpdate(ParamsRequest paramsRequest);

    /**
     * 查询参数详情信息
     * @param id
     * @return
     */
    Resp one(Integer id);
}

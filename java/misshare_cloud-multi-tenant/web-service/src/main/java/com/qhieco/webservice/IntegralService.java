package com.qhieco.webservice;

import com.qhieco.commonentity.Integral;
import com.qhieco.request.web.IntegralRequest;
import com.qhieco.response.Resp;

import java.util.List;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/5/24 10:32
 * <p>
 * 类说明：
 * ${description}
 */
public interface IntegralService {

    /**
     * 查询积分项列表（分页）
     * @param integralRequest 请求参数
     * @return Resp
     */
    Resp query(IntegralRequest integralRequest);

    /**
     * 新增或保存积分项信息
     * @param list 积分项对象集合
     * @return Resp
     * @throws Exception
     */
    Resp saveUpdate(List<Integral> list)throws Exception;

    /**
     * 根据积分项主键id 查询对象信息
     * @param id 主键ID
     * @return Resp
     */
    Resp FindIntegralOne(Integer id);

    /**
     * 修改积分项或积分系数
     * @param list
     * @return
     */
    Resp saveUpdateIntegral(List<IntegralRequest> list);
}

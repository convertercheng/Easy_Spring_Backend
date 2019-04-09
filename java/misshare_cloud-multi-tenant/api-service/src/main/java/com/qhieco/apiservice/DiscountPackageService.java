package com.qhieco.apiservice;

import com.qhieco.request.api.DiscountPackageOrderRequest;
import com.qhieco.request.api.DiscountPackageRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.api.DiscountPackageData;
import com.qhieco.response.data.api.DiscountPackageListData;

import java.util.List;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/7/18 13:57
 * <p>
 * 类说明：
 * 停车场优惠套餐service
 */
public interface DiscountPackageService {

    /**
     * 查询车场优惠套餐信息
     * @param request
     * @return
     */
    Resp getDiscountPackageInfo(DiscountPackageRequest request);

    /**
     * 购买优惠套餐信息接口
     * @param request
     * @return
     * @throws Exception
     */
    Resp savePackageOrder(DiscountPackageOrderRequest request)throws Exception;

    /**
     * 查询用户车牌购买的所有的可用套餐信息
     * @param userId
     * @return
     */
    public List<DiscountPackageListData> queryPackageList(Integer userId);
}

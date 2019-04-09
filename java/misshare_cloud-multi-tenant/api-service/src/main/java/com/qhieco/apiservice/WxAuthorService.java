package com.qhieco.apiservice;

import com.qhieco.request.api.WxBindRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.api.ParkingInfoRespData;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/25 17:31
 * <p>
 * 类说明：
 * ${description}
 */
public interface WxAuthorService {

    /**
     * 根据code和车场ID跳转不同的页面
     * @param code
     * @param parklotId
     * @param ip
     * @return
     */
    public String wxAuthorLocation(String code, String parklotId, String ip)throws Exception;

    /**
     * 根据code跳转不同的页面
     * @param code
     * @return
     */
    public String wxAuthorLocation(String code)throws Exception;

    /**
     * 绑定用户与openId并且跳转指定页面
     * @param wxBindRequest
     * @return
     * @throws Exception
     */
    public Resp saveUserBind(WxBindRequest wxBindRequest)throws Exception;

    /**
     * 根据openId查询订单信息
     * @param openId
     * @return
     * @throws Exception
     */
    public String wxAuthorOrderLocation(String openId)throws Exception;

    /**
     * 根据openId查询订单信息
     * @param orderId
     * @return
     * @throws Exception
     */
    public String wxUserOrderLocation(String orderId)throws Exception;

    public String scanpayAuthorRedirectUrl(String code, String parklotId) throws Exception ;

    public ParkingInfoRespData queryParkingInfo(String plateNo, Integer parklotId, String unionId, String openId);

    public String queryParkingInfoRedirect(String plateNo, Integer parklotId, String unionId, String openId) throws Exception;

    /**
     * 小程序获取用户信息和订单信息
     * @param wxBindRequest
     * @return Resp
     * @throws Exception
     */
    public Resp getSmallRoutineAuthorInfo(WxBindRequest wxBindRequest);

}

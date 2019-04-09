package com.qhieco.webservice;

import com.qhieco.request.web.CouponRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.CouponData;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/3/28 20:49
 * <p>
 * 类说明：
 * ${description}
 */
public interface CouponService {

    /**
     * 分页显示商家优惠券列表信息
     * @param couponRequest
     * @return
     */
    Resp query(CouponRequest couponRequest);

    /**
     * 商家优惠券列表导出Execl
     * @param couponRequest
     * @return
     */
    Resp execl(CouponRequest couponRequest, OutputStream outputStream, Class cl)throws IOException;

    /**
     * 保存优惠券
     * @return
     */
    Resp save(CouponRequest couponRequest);


//    /**
//     * 优惠券金额发放统计
//     * @param couponRequest
//     * @return
//     */
//    Resp report(CouponRequest couponRequest);

    /**
     * 绑定用户的优惠券列表
     * @param couponRequest
     * @return
     */
    Resp userCouponQuery(CouponRequest couponRequest);

    /**
     * 绑定用户优惠券列表导出Execl
     * @param couponRequest
     * @return
     */
    Resp userCouponExecl(CouponRequest couponRequest, OutputStream outputStream, Class cl)throws IOException;

}

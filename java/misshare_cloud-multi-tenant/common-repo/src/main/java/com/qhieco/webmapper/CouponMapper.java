package com.qhieco.webmapper;

import com.qhieco.commonentity.Coupon;
import com.qhieco.commonentity.Tag;
import com.qhieco.request.web.CouponRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.CouponCountData;
import com.qhieco.response.data.web.CouponData;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/3/28 20:50
 * <p>
 * 类说明：优惠券模块Mapper
 *
 */
@Mapper
public interface CouponMapper {

    /**
     * 分页显示优惠券列表信息
     * @param couponRequest
     * @return
     */
    List<CouponData> pageCoupon(CouponRequest couponRequest);

    /**
     * 获取总记录数
     * @param couponRequest
     * @return
     */
    Integer pageCouponTotalCount(CouponRequest couponRequest);

    /**
     * execl导出
     * @param couponRequest
     * @return
     */
    List<CouponData> execlCoupon(CouponRequest couponRequest);

    /**
     * 发放优惠券
     * @param couponRequest
     * @return
     */
    Resp saveCoupon(CouponRequest couponRequest);

    /**
     * 统计每天的发放金额
     * @return
     */
    List<Map<String,Object>> getDayCouponLimitCount();

    /**
     * 根据标签集合查询用户ID
     * @param tagIdList
     * @return
     */
    List<Tag> getTagByUserId(List<Integer> tagIdList);

    /**
     * 统计按天和类型分组优惠券发放和使用的金额
     * @param couponRequest
     * @return
     */
    List<CouponCountData> getCouponDayReport(CouponRequest couponRequest);
}

package com.qhieco.commonrepo;

import com.qhieco.commonentity.CouponTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/5 10:34
 * <p>
 * 类说明：
 *       优惠券模板表数据库交互层
 */

public interface CouponTemplateRepository extends JpaRepository<CouponTemplate, Integer> {


}

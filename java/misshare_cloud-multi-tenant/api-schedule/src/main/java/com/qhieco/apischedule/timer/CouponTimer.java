package com.qhieco.apischedule.timer;

import com.qhieco.commonrepo.CouponRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/5/22 11:27
 * <p>
 * 类说明：
 * ${说明}
 */
@Component
@Slf4j
public class CouponTimer {

    @Autowired
    private CouponRepository couponRepository;

    /**
     * 扫描优惠券是否过期
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void couponTimeOut() {
//        log.info(" ------------------- 执行couponTimeOut 定时器  start -------------------- ");
        couponRepository.updateTimeOutCoupon(System.currentTimeMillis());
//        log.info(" ------------------- 执行couponTimeOut 定时器  end -------------------- ");
    }
}

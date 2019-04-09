package com.qhieco.commonrepo;

import com.qhieco.commonentity.Plate;
import com.qhieco.commonentity.SMS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/2/5 下午11:03
 * <p>
 * 类说明：
 *     SMS数据交互层
 */

public interface SmsRepository extends JpaRepository<SMS, Integer> {

    /**
     * 返回SMS的集合
     * @param phone 手机号码
     * @return SMS list
     */
    List<SMS> findByPhoneOrderByIdDesc(String phone);

    /**
     * 返回三分钟内的向该号码发送的SMS
     * @param phone 手机号码
     * @param createTime 创建时间
     * @return SMS list
     */
    List<SMS> findByPhoneAndCreateTimeGreaterThan(String phone, Long createTime);
}

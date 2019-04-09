package com.qhieco.commonrepo;

import com.qhieco.commonentity.ApplyParkloc;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/6 10:34
 * <p>
 * 类说明：
 *       申请停车位数据库交互层
 */

public interface ApplyParklocRepository extends JpaRepository<ApplyParkloc, Integer> {

}

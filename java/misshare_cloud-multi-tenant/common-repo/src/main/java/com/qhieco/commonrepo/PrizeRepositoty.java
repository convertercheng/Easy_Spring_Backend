package com.qhieco.commonrepo;

import com.qhieco.commonentity.Prize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/6/12 16:36
 * <p>
 * 类说明：
 *  奖品repositoty
 */
public interface PrizeRepositoty extends JpaRepository<Prize, Integer>, JpaSpecificationExecutor<Prize> {
}

package com.qhieco.commonrepo;

import com.qhieco.commonentity.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/3 10:48
 * <p>
 * 类说明：
 * 商家信息数据交换层
 */
public interface BusinessRepository extends JpaRepository<Business,Integer>,JpaSpecificationExecutor<Business>{

    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update Business b set b.state=?2,b.businessStatus=?3 where b.id=?1")
    void deleteBusiness(Integer id, Integer state, Integer businessStatus);

    @Query("select b from Business b where b.businessName=?1 and b.state=?2")
    Business findBusinessByName(String businessName, Integer state) ;

    @Query("select b from Business b where b.businessName=?1 and b.id<>?2 and b.state=?3")
    Business findBusinessByName(String businessName, Integer id, Integer state) ;

    @Query("select b.id from Business b where b.businessName like %?1%")
    List<Integer> findBusinessByNameList(String businessName) ;
}

package com.qhieco.commonrepo;

import com.qhieco.commonentity.Advert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/6 10:53
 * <p>
 * 类说明：
 * ${说明}
 */
public interface AdvertRepository extends JpaRepository<Advert, Integer>, JpaSpecificationExecutor<Advert>{

    public Advert findByPhoneType(int phoneType);
}

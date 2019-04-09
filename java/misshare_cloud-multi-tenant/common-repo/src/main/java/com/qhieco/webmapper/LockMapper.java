package com.qhieco.webmapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/26 13:37
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface LockMapper {
    public int queryNameAdMacDuplicate(@Param("btName") String btName, @Param("btMac") String btMac, @Param("lockId") Integer lockId);
}

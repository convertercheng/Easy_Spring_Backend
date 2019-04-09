package com.qhieco.webmapper;

import com.qhieco.request.web.PropertyQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/14 11:48
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface PropertyCheckMapper {

    public Set<Integer> uniqueCheck(PropertyQuery query);

}

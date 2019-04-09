package com.qhieco.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/6/12 17:16
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface PrizeMapper {

    @Update(value = "UPDATE t_prize tp SET tp.`state`=0 WHERE  tp.`end_time` IS NOT NULL AND tp.`end_time`<UNIX_TIMESTAMP(NOW())*1000 AND tp.`state` IN(1, 9998)")
    public void updateTimeOutPrizeState();
}

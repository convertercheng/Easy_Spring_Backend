package com.qhieco.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/6/5 15:04
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface BalanceParklotMapper {

    @Update(value = "UPDATE t_balance_parklot SET balance=balance + #{balance} WHERE parklot_id= #{parklotId}")
    void updateParklotBalance(@Param("parklotId") Integer parklotId, @Param("balance") BigDecimal balance);
}

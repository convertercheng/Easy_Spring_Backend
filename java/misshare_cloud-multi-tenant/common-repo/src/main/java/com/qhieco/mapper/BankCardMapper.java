package com.qhieco.mapper;

import com.qhieco.response.data.api.UserWithdrawAmountRepData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/9 11:07
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface BankCardMapper {
    UserWithdrawAmountRepData queryBankCardInfoByUserId(@Param("userId") Integer userId, @Param("state") Integer state);
}

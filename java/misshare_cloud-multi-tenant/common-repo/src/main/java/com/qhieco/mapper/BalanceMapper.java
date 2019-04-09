package com.qhieco.mapper;

import com.qhieco.response.data.api.InvoiceLimitRepData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 17:22
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface BalanceMapper {

    InvoiceLimitRepData queryInvoiceAmountByUserId(@Param("userId") Integer userId);

    InvoiceLimitRepData queryParklotAdminInvoiceAmountByUserId(@Param("userId") Integer userId);
}

package com.qhieco.mapper;

import com.qhieco.response.data.api.InvoiceDetailRepData;
import com.qhieco.response.data.api.InvoiceLastWriteRepData;
import com.qhieco.response.data.api.InvoiceRepData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 16:14
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface InvoiceMapper {

    List<InvoiceRepData> queryInvoiceRecordListByUserId(@Param("userId") Integer userId, @Param("startPage") int startPage, @Param("pageSize") int pageSize);

    InvoiceDetailRepData queryInvoiceDetailById(@Param("invoiceId") Integer invoiceId);

    InvoiceLastWriteRepData queryInvoiceLastWriteInfoByUserId(@Param("userId") Integer userId);
}

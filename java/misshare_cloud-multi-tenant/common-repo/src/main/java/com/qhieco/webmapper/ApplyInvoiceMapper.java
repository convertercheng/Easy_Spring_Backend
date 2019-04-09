package com.qhieco.webmapper;

import com.qhieco.commonentity.ApplyInvoice;
import com.qhieco.request.web.ApplyInvoiceRequest;
import com.qhieco.webmapper.SqlBuilder.ApplySqlBuilder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-4-2 上午11:30
 * <p>
 * 类说明：
 * ${description}
 */
@Mapper
public interface ApplyInvoiceMapper {
    Integer applyCount(ApplyInvoiceRequest request);

    List<ApplyInvoice> applyPage(ApplyInvoiceRequest request);

    @SelectProvider(type = ApplySqlBuilder.class, method = "buildPage")
    List<ApplyInvoice> applyBuilder(ApplyInvoiceRequest request);

    @SelectProvider(type = ApplySqlBuilder.class, method = "buildCount")
    Integer countBuilder(ApplyInvoiceRequest request);

    List<ApplyInvoice> orderInvoiceExcel(ApplyInvoiceRequest request);
}

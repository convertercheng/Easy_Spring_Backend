package com.qhieco.apiservice.impl;

import com.qhieco.apiservice.InvoiceService;
import com.qhieco.config.ConfigurationFiles;
import com.qhieco.constant.Constants;
import com.qhieco.mapper.BalanceMapper;
import com.qhieco.mapper.InvoiceMapper;
import com.qhieco.mapper.UserMapper;
import com.qhieco.response.data.api.InvoiceDetailRepData;
import com.qhieco.response.data.api.InvoiceLastWriteRepData;
import com.qhieco.response.data.api.InvoiceLimitRepData;
import com.qhieco.response.data.api.InvoiceRepData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 16:13
 * <p>
 * 类说明：
 * ${说明}
 */
@Service
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceMapper invoiceMapper;

    @Autowired
    private BalanceMapper balanceMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ConfigurationFiles configurationFiles;

    @Override
    public List<InvoiceRepData> queryInvoiceRecordListByUserId(Integer userId, int pageNum) {
        int startPage = pageNum <= 0 ? 0 : pageNum * Constants.PAGE_SIZE;
        return invoiceMapper.queryInvoiceRecordListByUserId(userId, startPage, Constants.PAGE_SIZE);
    }

    @Override
    public InvoiceDetailRepData queryInvoiceDetailById(Integer invoiceId) {
        InvoiceDetailRepData invoiceDetailRepData = invoiceMapper.queryInvoiceDetailById(invoiceId);
        if (invoiceDetailRepData != null && !StringUtils.isEmpty(invoiceDetailRepData.getFilePath())) {
            invoiceDetailRepData.setFilePath(configurationFiles.getPicPath() + invoiceDetailRepData.getFilePath());
        }
        return invoiceDetailRepData;
    }

    @Override
    public InvoiceLastWriteRepData queryInvoiceLastWriteInfoByUserId(Integer userId) {
        return invoiceMapper.queryInvoiceLastWriteInfoByUserId(userId);
    }

    @Override
    public InvoiceLimitRepData queryInvoiceAmountByUserId(Integer userId) {
        InvoiceLimitRepData invoiceLimitRepData = null;
        int userType = userMapper.queryUserTypeByUserId(userId);

        if (userType == Constants.PARKING_ADMIN) {
            invoiceLimitRepData = balanceMapper.queryParklotAdminInvoiceAmountByUserId(userId);
        } else {
            invoiceLimitRepData = balanceMapper.queryInvoiceAmountByUserId(userId);
        }
        return invoiceLimitRepData;
    }
}

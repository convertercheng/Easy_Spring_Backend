package com.qhieco.apiservice.impl;

import com.qhieco.apiservice.WithdrawService;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.mapper.WithdrawMapper;
import com.qhieco.response.data.api.WithdrawRecordRespData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/14 10:44
 * <p>
 * 类说明：
 * 提现模块的service
 */
@Service
public class WithdrawServiceImpl implements WithdrawService {

    @Autowired
    private WithdrawMapper withdrawMapper;

    @Override
    public List<WithdrawRecordRespData> queryWithdrawRecordList(Integer userId, Integer pageNum) {
        int startPage = pageNum * Constants.PAGE_SIZE;
        List<WithdrawRecordRespData> withdrawRecordRespDataList = withdrawMapper.queryWithdrawRecordListByUserId(userId,
                Status.Withdraw.PROCESSING.getInt(), startPage, Constants.PAGE_SIZE);
        return withdrawRecordRespDataList;
    }
}

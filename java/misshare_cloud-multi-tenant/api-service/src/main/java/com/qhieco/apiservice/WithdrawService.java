package com.qhieco.apiservice;

import com.qhieco.response.data.api.WithdrawRecordRespData;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/14 10:42
 * <p>
 * 类说明：
 * ${说明}
 */
public interface WithdrawService {
    /**
     * 查询用户提现记录列表
     * @param userId
     * @param pageNum
     * @return
     */
    List<WithdrawRecordRespData> queryWithdrawRecordList(Integer userId, Integer pageNum);
}

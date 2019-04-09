package com.qhieco.request.api;

import lombok.Data;

import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 15:56
 * <p>
 * 类说明：
 * 地锁控制请求参数
 */
@Data
public class LockBatchControlRequest extends AbstractRequest{
    private String lock_ids;
    private Integer command;
    private List<Integer> lockList;

    public LockBatchControlRequest() {
    }

    public LockBatchControlRequest(Integer command, List<Integer> lockList, String time) {
        this.command = command;
        this.lockList = lockList;
        this.setTimestamp(time);
    }
}

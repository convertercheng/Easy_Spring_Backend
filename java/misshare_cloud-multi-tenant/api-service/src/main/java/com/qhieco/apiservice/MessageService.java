package com.qhieco.apiservice;

import com.qhieco.response.data.api.MessageRespData;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/14 14:00
 * <p>
 * 类说明：
 * ${说明}
 */
public interface MessageService {

    /**
     * 用户消息列表
     *
     * @param userId
     * @param pageNum
     * @return
     */
    List<MessageRespData> queryMessageListByUserId(Integer userId, int pageNum, int type);
}

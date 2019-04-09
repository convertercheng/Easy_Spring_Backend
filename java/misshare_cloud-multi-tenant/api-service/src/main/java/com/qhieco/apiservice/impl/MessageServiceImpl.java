package com.qhieco.apiservice.impl;

import com.qhieco.apiservice.MessageService;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.mapper.MessageMapper;
import com.qhieco.response.data.api.MessageRespData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/14 14:01
 * <p>
 * 类说明：
 * ${说明}
 */
@Service
@Slf4j
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageMapper messageMapper;

    @Override
    public List<MessageRespData> queryMessageListByUserId(Integer userId, int pageNum, int type) {
        int startPage = pageNum * Constants.PAGE_SIZE;
        return messageMapper.queryMessageListByUserId(userId, type, Status.Common.VALID.getInt(), startPage, Constants.PAGE_SIZE);
    }
}

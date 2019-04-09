package com.qhieco.apiservice.delaymessage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/6/5 下午1:39
 * <p>
 * 类说明：
 *     延时消息处理队列
 */
@Slf4j
@Service
public class DelayMessageServiceImpl {

    DelayMessageServiceImpl() {
        log.info("init CycleQueue");
        CycleQueue cycleQueue = CycleQueue.getInstance();
        cycleQueue.startScan();
    }
}

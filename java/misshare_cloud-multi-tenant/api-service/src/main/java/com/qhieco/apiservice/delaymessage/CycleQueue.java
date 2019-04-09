package com.qhieco.apiservice.delaymessage;

import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/6/5 上午11:08
 * <p>
 * 类说明：
 *     环形队列，实现消息延迟机制
 */
@Slf4j
public class CycleQueue {

    public static final Integer TOTAL_LEN = 60;

    private static final Integer ONE_SECOND = 1000;

    private Slot[] cycleQueue;

    private Integer currentSlot;

    private Timer timer = new Timer();

    private CycleQueue(){
        currentSlot = 0;
        cycleQueue = new Slot[TOTAL_LEN];
    }

    private static class CircleQueueHelper {
        private static final CycleQueue CIRCIE_QUEUE = new CycleQueue();
    }

    public static CycleQueue getInstance() {
        return CircleQueueHelper.CIRCIE_QUEUE;
    }

    void startScan() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                currentSlot = (++currentSlot) % TOTAL_LEN;
                if (cycleQueue[currentSlot] == null) {
                    cycleQueue[currentSlot] = new Slot();
                }
                Set<Task> taskSet = cycleQueue[currentSlot].getTaskSet();
                for (Task task : taskSet) {
                    log.info("current task name is {}" + task.getName());
                    Integer cycleNum = task.getCycleNum();
                    if (cycleNum == 0) {
                        taskSet.remove(task);
                        ExecutorService executorService = Executors.newSingleThreadExecutor();
                        executorService.execute(() -> {
                            task.getTaskPointer().execute();
                        });
                        executorService.shutdown();
                    } else {
                        cycleNum--;
                        task.setCycleNum(cycleNum);
                    }
                }
            }
        }, ONE_SECOND, ONE_SECOND);
    }

    public Integer getCurrentSlot() {
        return currentSlot;
    }

    public Slot[] getCycleQueue() {
        return cycleQueue;
    }
}

package com.qhieco.apiservice.delaymessage;

import java.util.HashSet;
import java.util.Set;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/6/5 下午2:11
 * <p>
 * 类说明：
 * ${description}
 */

public class Slot {

    private Set<Task> taskSet;

    public Slot() {
        taskSet = new HashSet<>();
    }

    public Set<Task> getTaskSet() {
        return taskSet;
    }

    @Override
    public String toString() {
        return "Slot{" +
                "taskSet=" + taskSet +
                '}';
    }
}

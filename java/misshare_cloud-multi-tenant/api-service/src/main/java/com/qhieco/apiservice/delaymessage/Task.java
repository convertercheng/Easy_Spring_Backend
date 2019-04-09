package com.qhieco.apiservice.delaymessage;

import java.util.Objects;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/6/5 下午2:05
 * <p>
 * 类说明：
 * ${description}
 */

public class Task {

    private String name;

    private Integer cycleNum;

    private TaskPointer taskPointer;

    public Task(String name, Integer cycleNum, TaskPointer taskPointer) {
        this.name = name;
        this.cycleNum = cycleNum;
        this.taskPointer = taskPointer;
    }

    TaskPointer getTaskPointer() {
        return taskPointer;
    }

    String getName() {
        return name;
    }

    Integer getCycleNum() {
        return cycleNum;
    }

    void setCycleNum(Integer cycleNum) {
        this.cycleNum = cycleNum;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", cycleNum=" + cycleNum +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Task task = (Task) o;
        return Objects.equals(name, task.name) &&
                Objects.equals(taskPointer, task.taskPointer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, taskPointer);
    }
}

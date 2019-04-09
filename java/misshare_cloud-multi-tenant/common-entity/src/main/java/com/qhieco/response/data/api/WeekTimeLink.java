package com.qhieco.response.data.api;

import lombok.Data;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/26 16:43
 * <p>
 * 类说明：
 * 此类用来保存循环发布情况下的一周的时间
 */
@Data
public class WeekTimeLink {
    /**
     * 发布的时间段的开始时间
     */
    private long startTime;
    /**
     * 发布的时间段的结束时间
     */
    private long endTime;

    private int day;

    private WeekTimeLink next;

    public WeekTimeLink() {
    }

    public WeekTimeLink(long startTime, long endTime, int day) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
    }

    public void add(long startTime, long endTime, int day) {
        WeekTimeLink node = new WeekTimeLink(startTime, endTime, day);
        if (this.next == null) {
            this.next = node;
        } else {
            this.next.addNode(node);
        }
    }

    public void addNode(WeekTimeLink node) {
        if (this.next == null) {
            this.next = node;
        } else {
            this.next.addNode(node);
        }
    }

    public WeekTimeLink getNodeByIndex(int index) {
        int pos = 0;
        WeekTimeLink node = this.next;
        while (pos != index) {
            node = node.next;
            pos++;
        }
        return node;
    }

    public void print() {
        System.out.println(" --------------- " + this.next);
        if (this.next != null) {
            this.next.print();
        }
    }

    public static void main(String[] args) {
        String dayOfWeek = "0,1,3,6";
        long startTime = 1521972000000L;
        long endTime = 1521997200000L;

        WeekTimeLink weekTimeLinkHead = new WeekTimeLink();
        for (int i = 0; i < 7; i++) {
            if (dayOfWeek.contains(String.valueOf(i))) {
                weekTimeLinkHead.add( startTime, endTime, i);
            } else {
                weekTimeLinkHead.add(0, 0, i);
            }
        }
        weekTimeLinkHead.print();

        System.out.println("  ========== " + weekTimeLinkHead.getNodeByIndex(0));
    }
}

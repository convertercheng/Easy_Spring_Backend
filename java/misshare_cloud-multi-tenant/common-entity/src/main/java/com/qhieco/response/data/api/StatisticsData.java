package com.qhieco.response.data.api;

import lombok.Data;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/7/17 17:25
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class StatisticsData {

    private StatisticsBean statistics;

    public StatisticsBean getUser() {
        return statistics;
    }

    public void setUser(StatisticsBean user) {
        this.statistics = user;
    }


    public static class StatisticsBean {
        /**
         * 基本信息
         */
        private int id;
        private int activityId;
        private int userId;
        private int type;
        private int value;
        private long createTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getActivityId() {
            return activityId;
        }

        public void setActivityId(int activityId) {
            this.activityId = activityId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }
    }
}

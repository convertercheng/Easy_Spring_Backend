package com.qhieco.barrier;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/4/2 11:43
 * <p>
 * 类说明：
 * ${desription}
 */
public class RegisterTempResponse {


    /**
     * code : 0
     * message : ok
     * data : {"id":366,"park":100027,"license":"粤ESB411","created":false,"created_at":"2018-04-03 12:16:05","last_end_time":"2018-04-03 12:16:00","begin_time":"2017-08-22 00:00:00","end_time":"2017-12-21 23:59:59","type":0,"amount":0,"remark":"","duration":""}
     */

    private int code;
    private String message;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 366
         * park : 100027
         * license : 粤ESB411
         * created : false
         * created_at : 2018-04-03 12:16:05
         * last_end_time : 2018-04-03 12:16:00
         * begin_time : 2017-08-22 00:00:00
         * end_time : 2017-12-21 23:59:59
         * type : 0
         * amount : 0
         * remark :
         * duration :
         */

        private int id;
        private int park;
        private String license;
        private boolean created;
        private String created_at;
        private String last_end_time;
        private String begin_time;
        private String end_time;
        private int type;
        private int amount;
        private String remark;
        private String duration;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPark() {
            return park;
        }

        public void setPark(int park) {
            this.park = park;
        }

        public String getLicense() {
            return license;
        }

        public void setLicense(String license) {
            this.license = license;
        }

        public boolean isCreated() {
            return created;
        }

        public void setCreated(boolean created) {
            this.created = created;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getLast_end_time() {
            return last_end_time;
        }

        public void setLast_end_time(String last_end_time) {
            this.last_end_time = last_end_time;
        }

        public String getBegin_time() {
            return begin_time;
        }

        public void setBegin_time(String begin_time) {
            this.begin_time = begin_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }
    }
}

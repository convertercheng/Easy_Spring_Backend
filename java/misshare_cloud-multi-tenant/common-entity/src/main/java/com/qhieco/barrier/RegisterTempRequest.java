package com.qhieco.barrier;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/4/2 11:40
 * <p>
 * 类说明：
 *     新增/续期固定车（黑白名单/临卡）
 */
public class RegisterTempRequest {


    /**
     * vehicle : 粤ESB411
     * parkId : 1
     * orderType : month
     * begin_time : 2017-08-22 00:00:00
     * end_time : 2017-12-21 23:59:59
     * info : {"name":"ew","phone":"12","address":""}
     */

    private String vehicle;
    private int parkId;
    private String orderType;
    private String begin_time;
    private String end_time;
    private InfoBean info;

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public int getParkId() {
        return parkId;
    }

    public void setParkId(int parkId) {
        this.parkId = parkId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
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

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * name : ew
         * phone : 12
         * address :
         */

        private String name;
        private String phone;
        private String address;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        @Override
        public String toString() {
            return "InfoBean{" +
                    "name='" + name + '\'' +
                    ", phone='" + phone + '\'' +
                    ", address='" + address + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "RegisterTempRequest{" +
                "vehicle='" + vehicle + '\'' +
                ", parkId=" + parkId +
                ", orderType='" + orderType + '\'' +
                ", begin_time='" + begin_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", info=" + info +
                '}';
    }
}

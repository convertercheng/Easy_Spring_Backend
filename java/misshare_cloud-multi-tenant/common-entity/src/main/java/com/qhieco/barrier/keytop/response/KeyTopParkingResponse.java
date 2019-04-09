package com.qhieco.barrier.keytop.response;

import lombok.Data;

import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/4/26 11:18
 * <p>
 * 类说明：
 * ${desription}
 */
@Data
public class KeyTopParkingResponse {


    /**
     * resCode : 0
     * resMsg :
     * data : [{"orderNo":"2145231232421321","keepTime":"2015 - 06 - 24 14: 00: 00"}]
     */

    private int resCode;
    private String resMsg;
    private List<DataBean> data;

    @Data
    public static class DataBean {
        /**
         * orderNo : 2145231232421321
         * keepTime : 2015 - 06 - 24 14: 00: 00
         */

        private String orderNo;
        private String keepTime;
    }
}

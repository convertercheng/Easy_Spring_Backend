package com.qhieco.request.api;

import lombok.Data;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/2/27 下午6:36
 * <p>
 * 类说明：
 *     附近空车位的请求接口
 */

@Data
public class ParklotNearByRequest {


    /**
     * map : {"lat":"xxx","lng":"xxx"}
     * locate : {"lat":"xx","lng":"xx"}
     * radius : xx
     * timestamp : xxxx
     */

    private MapBean map;
    private LocateBean locate;
    private Integer radius;
    private String timestamp;


    @Data
    public static class MapBean {
        /**
         * lat : xxx
         * lng : xxx
         */

        private Double lat;
        private Double lng;

    }

    @Data
    public static class LocateBean {
        /**
         * lat : xx
         * lng : xx
         */

        private Double lat;
        private Double lng;

    }
}

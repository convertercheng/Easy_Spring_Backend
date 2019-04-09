package com.qhieco.response.data.web;

import lombok.Data;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/4/6 下午9:31
 * <p>
 * 类说明：
 *      车位发布数据
 */
@Data
public class ParklocPublishData {

    private String startTime;

    private String endTime;

    private Integer mode;

    private String dayOfWeeks;

    private Integer state;

    public ParklocPublishData(String startTime, String endTime, Integer mode, String dayOfWeeks, Integer state) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.mode = mode;
        this.dayOfWeeks = dayOfWeeks;
        this.state = state;
    }
}

package com.qhieco.request.api;

import lombok.Data;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/15 9:05
 * <p>
 * 类说明：
 *
 */
@Data
public class AllowPublishCountQueryRequest extends AbstractRequest {
    private Integer user_id;
    private Long start_time;
    private Long end_time;
    private Integer mode;
    private String day_of_week;
}

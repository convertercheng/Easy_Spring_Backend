package com.qhieco.request.api;

import lombok.Data;

import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/26 10:39
 * <p>
 * 类说明：
 *       发布类
 */
@Data
public class PublishAddRequest extends AbstractRequest {

    private Integer parkloc_id;

    private Integer user_id;

    private Long start_time;

    private Long end_time;

    private Integer mode;

    private Integer parklot_id;

    private String day_of_week;



}

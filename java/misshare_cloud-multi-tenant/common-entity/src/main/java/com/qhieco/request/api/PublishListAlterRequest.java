package com.qhieco.request.api;

import lombok.Data;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/26 10:39
 * <p>
 * 类说明：
 *       发布类
 */
@Data
public class PublishListAlterRequest extends AbstractRequest {

    private String publishIds;

    private Long start_time;

    private Long end_time;

    private Integer mode;

    private Integer parklot_id;

    private String day_of_week;



}

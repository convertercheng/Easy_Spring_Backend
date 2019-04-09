package com.qhieco.request.api;

import lombok.Data;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 15:56
 * <p>
 * 类说明：
 * 预约车位请求参数
 */
@Data
public class ConfirmRequest extends AbstractRequest{
    private Integer user_id;
    private Integer share_id;
    private Long share_startTime;
    private Long share_endTime;
    private Integer parkloc_id;
    private Integer district_id;
    private Integer parklot_id;
    private Integer plate_id;
    private Long start_time;
    private Long end_time;
    private Integer isImmediatelyEnter;
}

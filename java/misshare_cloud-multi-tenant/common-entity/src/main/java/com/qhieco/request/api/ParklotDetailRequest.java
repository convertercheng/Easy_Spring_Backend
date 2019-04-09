package com.qhieco.request.api;

import lombok.Data;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/26 10:39
 * <p>
 * 类说明：
 *       设置常用停车场请求类
 */
@Data
public class ParklotDetailRequest extends AbstractRequest {


    /**
     * parklotId : 2
     * lng : 22.22
     * lat : 22.22
     */

    private Integer parklot_id;

    private Double lng;

    private Double lat;

}

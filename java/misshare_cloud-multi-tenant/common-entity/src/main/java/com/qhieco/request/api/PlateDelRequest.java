package com.qhieco.request.api;

import lombok.Data;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/1 下午4:11
 * <p>
 * 类说明：
 *     删除车牌号请求类
 */
@Data
public class PlateDelRequest extends AbstractRequest {


    /**
     * user_id : 1
     * plate_id : 1,2,3,4
     */

    private int user_id;
    private String plate_id;

}

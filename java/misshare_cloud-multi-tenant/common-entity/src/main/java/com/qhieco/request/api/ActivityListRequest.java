package com.qhieco.request.api;

import lombok.Data;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/4 12:41
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class ActivityListRequest extends AbstractRequest {
    /**
     * 1：长图 首页显示， 2：宽图 列表显示
     * 参考status.ActivityFileState
     */
    private Integer state;

    private Integer page_num;
}

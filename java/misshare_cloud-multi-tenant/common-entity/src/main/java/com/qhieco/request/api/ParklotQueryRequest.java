package com.qhieco.request.api;

import lombok.Data;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/2/24 15:56
 * <p>
 * 类说明：
 *     停车场搜索
 */
@Data
public class ParklotQueryRequest extends AbstractRequest{
    private Double x;
    private Double y;
    private String keywords;
    private Integer page_size;
    private Integer page_num;
}

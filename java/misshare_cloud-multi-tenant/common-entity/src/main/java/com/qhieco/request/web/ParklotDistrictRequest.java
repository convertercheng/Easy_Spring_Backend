package com.qhieco.request.web;

import lombok.Data;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 18/7/06 下午5:20
 * <p>
 * 类说明：
 */
@Data
public class ParklotDistrictRequest extends QueryPaged{
    private String parklotName;
    private String districtName;
}

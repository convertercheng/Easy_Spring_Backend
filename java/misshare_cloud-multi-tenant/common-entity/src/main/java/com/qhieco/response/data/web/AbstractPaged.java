package com.qhieco.response.data.web;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-8 上午10:07
 * <p>
 * 类说明：
 * 　抽象分页类
 */
@Data
@Builder
public class AbstractPaged<T> {

    private Integer sEcho;
    private Integer iTotalRecords;
    private Integer iTotalDisplayRecords;
    private List<T> dataList;

}

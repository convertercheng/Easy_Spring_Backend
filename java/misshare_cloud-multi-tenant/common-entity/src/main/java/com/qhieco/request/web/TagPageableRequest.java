package com.qhieco.request.web;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/4/6 9:43
 * <p>
 * 类说明：
 * 标签分页查询类
 */
@Data
public class TagPageableRequest extends  QueryPaged{
    String name;
}

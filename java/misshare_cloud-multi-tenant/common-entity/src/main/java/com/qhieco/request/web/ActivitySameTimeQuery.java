package com.qhieco.request.web;

import lombok.Data;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/4/14 11:29
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class ActivitySameTimeQuery{
    private long beginTime;
    private long endTime;
    private Integer type;
    private Integer id;
}

package com.qhieco.util;

import weixin.popular.bean.card.get.AbstractResult;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/5/15 18:20
 * <p>
 * 类说明：
 * ${description}
 */
public class ResultState extends AbstractResult {
    private static final long serialVersionUID = 1692432930341768342L;
    private int errcode; // 状态
    private String errmsg; //信息
}

package com.qhieco.util;

import com.qhieco.constant.Constants;
import org.apache.commons.lang.StringUtils;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/4/3 下午3:08
 * <p>
 * 类说明：
 *     字符串工具类
 */

public class QhieStringUtil {

    public static String removeSpace(String str) {
        if (StringUtils.isEmpty(str)) {
            return Constants.EMPTY_STRING;
        }
        return str.replace(" ", "");
    }

}

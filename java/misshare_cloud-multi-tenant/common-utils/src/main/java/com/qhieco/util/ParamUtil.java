package com.qhieco.util;

import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import org.springframework.util.StringUtils;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/4/2 上午9:22
 * <p>
 * 类说明：
 *     参数校验工具类
 */

public class ParamUtil {

    public static boolean isInValidDayOfWeeks(Integer mode, String dayOfWeek) {
        if (Status.PublishMode.ONCE.getInt().equals(mode) && !StringUtils.isEmpty(dayOfWeek)) {
            return true;
        } else if (Status.PublishMode.LOOP.getInt().equals(mode)) {
            if (StringUtils.isEmpty(dayOfWeek)) {
                return true;
            }
            for (String ele: dayOfWeek.split(Constants.DELIMITER_COMMA)) {
                Integer eleInt = Integer.valueOf(ele);
                if (eleInt < 0 || eleInt > 6) {
                    return true;
                }
            }
        }
        return false;
    }

}

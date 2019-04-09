package com.qhieco.util;

import com.qhieco.constant.Constants;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/2/26 上午12:07
 * <p>
 * 类说明：
 *     常用的工具类
 */
@Slf4j
public class CommonUtil {

    /**
     * 检测客户端传来的时间戳的合法性
     * @param timeStamp 客户端传入的时间戳
     */
    public static boolean isTimeStampInValid(String timeStamp) {
        try {
            Long clientTime = Long.valueOf(timeStamp);
            Long serverTime = System.currentTimeMillis();
            return clientTime > serverTime + Constants.TIMESTAMP_MAX_DIFF_C_AND_S
                    || clientTime < serverTime - Constants.TIMESTAMP_MAX_DIFF_C_AND_S;
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
            return true;
        }
    }

    /**
     * 检测极光推送id的合法性
     * @param jpushId 极光推送id
     * @return 是非法的极光id
     */
    public static boolean isJpushIdInValid(String jpushId) {
        return false;
    }

    /**
     * 设置保留的小数位数
     * @param bitAfterDot 小数位数
     * @param num 传入的double类型
     * @return double
     */
    public static double retainBitDecimal(int bitAfterDot, double num) {
        BigDecimal bigDecimal = new BigDecimal(num);
        return bigDecimal.setScale(bitAfterDot, BigDecimal.ROUND_HALF_DOWN).doubleValue();
    }

}

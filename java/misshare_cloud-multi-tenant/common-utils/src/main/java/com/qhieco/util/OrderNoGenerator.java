package com.qhieco.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/1 下午9:07
 * <p>
 * 类说明：
 *     订单号生成器
 *
 * 订单命名规定：
 *     1. 唯一性
 *     2. 安全性：不能透露公司的真实运营信息
 *     3. 不能使用大规模随机码，可使用小规模的随机码 2～3位
 *     4. 防止并发，针对编码中有时间的设定
 *     5. 控制位数，订单号的作用是便于查询，用户将订单号报给客服，由客服查询，一般在10～15位，淘宝18位，京东11位
 */

public class OrderNoGenerator {

    private static final int BIT_EIGHT = 8;
    private static final int BIT_FOUR = 4;
    private static final int BIT_THREE = 3;
    private static final String FORMAT_USER_ID = "%04d";

    /**
     * 返回订单号，必要时加一层缓存解决锁表问题
     * 生成规则：
     *
     *   订单号 = 订单类型（1）+ 时间信息4位(4) + UNIX时间戳后8位(8）+  + 用户id后4位（4） + 随机数3位（3）, 20位
     *
     * @param orderType 订单类型 0：预约订单 1：停车订单 2：退款订单 3：充值订单 4：提现订单
     * @return 订单号
     */
    public static String getOrderNo(int orderType, String userId) {
        String timeInfo = new SimpleDateFormat("MMdd").format(new Date());
        String timestamp = String.valueOf(System.currentTimeMillis());
        int len = timestamp.length();
        String timeStampLast8bit = timestamp.substring(len - BIT_EIGHT, len);
        String randomCode = RandomStringUtils.random(BIT_THREE, false, true);
        String userId4Bit = getUserId(userId);
        return orderType + timeInfo + timeStampLast8bit + userId4Bit + randomCode;
    }

    /**
     * 生成4位数的userId
     * @param userId 用户id
     * @return userId
     */
    private static String getUserId(String userId) {
        int len = userId.length();
        if (userId.length() < BIT_FOUR) {
            return String.format(FORMAT_USER_ID, Integer.valueOf(userId));
        } else {
            return userId.substring(len - BIT_FOUR, len);
        }
    }

    public static void main(String[] args) {
        System.out.println(getOrderNo(1, "999"));
    }

}

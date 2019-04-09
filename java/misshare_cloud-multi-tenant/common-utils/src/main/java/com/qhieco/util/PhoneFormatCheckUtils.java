package com.qhieco.util;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/2/25 下午11:51
 * <p>
 * 类说明：
 *     验证手机号码的合法性
 */

public class PhoneFormatCheckUtils {

    /**
     * 验证手机号码的合法性
     * @param phone 手机号码
     * @return 是否合法
     */
    public static boolean isPhoneLegal(String phone) {
        return !StringUtils.isEmpty(phone) && checkCellphone(phone);
    }

    /**
     * 先使用较为粗糙的正则匹配一下，此处可以改善
     * @param phone 手机号码
     * @return 是否合法
     */
    private static boolean checkCellphone(String phone) {
        String regex = "^1[3|4|5|7|8][0-9]{9}$";
        return check(phone, regex);
    }

    private static boolean check(String phone, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    public static void main(String[] args) {
        boolean isLegal = isPhoneLegal("11111111111");
        System.out.println(isLegal);
    }

}

package com.qhieco.common;

import com.qhieco.constant.Constants;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/25 下午2:20
 * <p>
 * 类说明：
 *     货币金额类，包含：
 *     加 add
 *     减 subtract
 *     乘 multiply
 *     除 divide
 *     比较 greaterThan equals
 *     等工具类方法
 */

public class QhieCurrency {

    private BigDecimal bigDecimal;

    public static final int DEFAULT_SCALE = 2;

    private QhieCurrency(String val){
        if (StringUtils.isEmpty(null == val? null: val.trim())){
            bigDecimal = BigDecimal.ZERO;
        } else {
            bigDecimal = new BigDecimal(val.trim());
        }
    }

    /**
     * 创建金额类
     * @param val 金额字符串
     * @return QhieCurrency 金额类
     */
    public static QhieCurrency getInstance(String val) {
        return new QhieCurrency(val);
    }

    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }

    private void setBigDecimal(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }

    /**
     * 比较：返回是否货币A大于货币B值
     * @param qhieCurrencyA 货币类A
     * @param qhieCurrencyB 货币类B
     * @return 如果大于返回true，如果小于返回false
     */
    public static boolean greaterThan(QhieCurrency qhieCurrencyA, QhieCurrency qhieCurrencyB) {
        return Constants.MIN_NON_NEGATIVE_INTEGER < qhieCurrencyA.bigDecimal.compareTo(qhieCurrencyB.bigDecimal);
    }

    /**
     * 比较：返回货币A值是否等于货币B值
     * @param qhieCurrencyA 货币类A
     * @param qhieCurrencyB 货币类B
     * @return 如果相等返回true，如果不等返回false
     */
    public static boolean equals(QhieCurrency qhieCurrencyA, QhieCurrency qhieCurrencyB) {
        return Constants.MIN_NON_NEGATIVE_INTEGER == qhieCurrencyA.bigDecimal.compareTo(qhieCurrencyB.bigDecimal);
    }

    /**
     * 加：返回结果精确到小数点后2位，舍入模式：四舍五入
     * @param qhieCurrencyA qhieCurrencyA
     * @param qhieCurrencyB qhieCurrencyB
     * @return 返回相加结果
     */
    public static QhieCurrency add(QhieCurrency qhieCurrencyA, QhieCurrency qhieCurrencyB) {
        return add(qhieCurrencyA, qhieCurrencyB, DEFAULT_SCALE);
    }

    /**
     * 加：返回结果精确到小数点后scale位，舍入模式：四舍五入
     * @param qhieCurrencyA qhieCurrencyA
     * @param qhieCurrencyB qhieCurrencyB
     * @param scale 返回结果的精确度，设置返回结果精确到小数点后几位
     * @return 返回相加结果
     */
    public static QhieCurrency add(QhieCurrency qhieCurrencyA, QhieCurrency qhieCurrencyB, int scale) {
        QhieCurrency qhieCurrency = QhieCurrency.getInstance("");
        qhieCurrency.setBigDecimal(qhieCurrencyA.getBigDecimal().add(qhieCurrencyB.getBigDecimal()).setScale(scale, RoundingMode.HALF_UP));
        return qhieCurrency;
    }

    /**
     * 减：返回结果精确到小数点后2位，舍入模式：四舍五入
     * @param qhieCurrencyA qhieCurrencyA
     * @param qhieCurrencyB qhieCurrencyB
     * @return 返回相减结果
     */
    public static QhieCurrency subtract(QhieCurrency qhieCurrencyA, QhieCurrency qhieCurrencyB) {
        return subtract(qhieCurrencyA, qhieCurrencyB, DEFAULT_SCALE);
    }

    /**
     * 减：返回结果精确到小数点后scale位，舍入模式：四舍五入
     * @param qhieCurrencyA qhieCurrencyA
     * @param qhieCurrencyB qhieCurrencyB
     * @param scale 返回结果的精确度，设置返回结果精确到小数点后几位
     * @return 返回相减结果
     */
    public static QhieCurrency subtract(QhieCurrency qhieCurrencyA, QhieCurrency qhieCurrencyB, int scale) {
        QhieCurrency qhieCurrency = QhieCurrency.getInstance("");
        qhieCurrency.setBigDecimal(qhieCurrencyA.getBigDecimal().add(qhieCurrencyB.getBigDecimal().negate()).setScale(scale, RoundingMode.HALF_UP));
        return qhieCurrency;
    }

    /**
     * 乘：返回结果精确到小数点后2位，舍入模式：四舍五入
     * @param qhieCurrencyA qhieCurrencyA
     * @param qhieCurrencyB qhieCurrencyB
     * @return 返回相乘结果
     */
    public static QhieCurrency multiply(QhieCurrency qhieCurrencyA, QhieCurrency qhieCurrencyB) {
        return multiply(qhieCurrencyA, qhieCurrencyB, DEFAULT_SCALE);
    }

    /**
     * 乘：返回结果精确到小数点后scale位，舍入模式：四舍五入
     * @param qhieCurrencyA qhieCurrencyA
     * @param qhieCurrencyB qhieCurrencyB
     * @param scale 返回结果的精确度，设置返回结果精确到小数点后几位
     * @return 返回相乘结果
     */
    public static QhieCurrency multiply(QhieCurrency qhieCurrencyA, QhieCurrency qhieCurrencyB, int scale) {
        QhieCurrency qhieCurrency = QhieCurrency.getInstance("");
        qhieCurrency.setBigDecimal(qhieCurrencyA.getBigDecimal().multiply(qhieCurrencyB.getBigDecimal()).setScale(scale, RoundingMode.HALF_UP));
        return qhieCurrency;
    }

    /**
     * 除：返回结果精确到小数点后2位，舍入模式：四舍五入
     * @param qhieCurrencyA qhieCurrencyA
     * @param qhieCurrencyB qhieCurrencyB
     * @return 返回相除结果
     */
    public static QhieCurrency divide(QhieCurrency qhieCurrencyA, QhieCurrency qhieCurrencyB) {
        return divide(qhieCurrencyA, qhieCurrencyB, DEFAULT_SCALE);
    }

    /**
     * 除：返回结果精确到小数点后scale位，舍入模式：四舍五入
     * @param qhieCurrencyA qhieCurrencyA
     * @param qhieCurrencyB qhieCurrencyB
     * @param scale 返回结果的精确度，设置返回结果精确到小数点后几位
     * @return 返回相除结果
     */
    public static QhieCurrency divide(QhieCurrency qhieCurrencyA, QhieCurrency qhieCurrencyB, int scale) {
        QhieCurrency qhieCurrency = QhieCurrency.getInstance("");
        if (Constants.MIN_NON_NEGATIVE_INTEGER == BigDecimal.ZERO.compareTo(qhieCurrencyB.getBigDecimal())) {
            throw new ArithmeticException("除数不能为0");
        }
        qhieCurrency.setBigDecimal(qhieCurrencyA.getBigDecimal().divide(qhieCurrencyB.getBigDecimal(), scale, RoundingMode.HALF_UP));
        return qhieCurrency;
    }

}

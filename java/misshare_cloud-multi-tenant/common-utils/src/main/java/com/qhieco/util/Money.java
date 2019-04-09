package com.qhieco.util;

import com.qhieco.constant.Constants;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by xujiayu on 17/9/18.
 */
public class Money {
    public static int CNYToChinaFen(BigDecimal CNY) {
        return (Constants.BIGDECIMAL_ONE_HUNDRED.multiply(CNY).intValue());
    }

    public static BigDecimal ChinaFenToCNY(BigDecimal ChinaFen) {
        return ChinaFen.divide(Constants.BIGDECIMAL_ONE_HUNDRED);
    }

    public static BigDecimal BigDecimal(List<BigDecimal> fees) {
        BigDecimal sum = Constants.BIGDECIMAL_ZERO;
        for (BigDecimal fee: fees) {
            sum = sum.add(fee);
        }
        return sum;
    }
}

package com.qhieco.util;

import com.qhieco.constant.Constants;

import java.math.BigDecimal;

/**
 * Created by liuyuan on 17/11/14
 */
public class NumberUtils {
	/**
	 * 
	 * @param d
	 * @return
	 */
	public static BigDecimal format(BigDecimal d) {
		if(d != null){
			return d.setScale(Constants.DECIMAL_PLACE_DEFAULT, BigDecimal.ROUND_HALF_UP);
		}
		return Constants.BIGDECIMAL_ZERO;
	}
	
	/**
	 * double类型的数据求和，精确保留{FORMAT_SCALE}位有效数字，向上取值
	 */
	public static BigDecimal add(BigDecimal...args) {
		BigDecimal result = BigDecimal.ZERO;
		if(args != null) {
			for (int i = 0; i < args.length; i++) {
				if(args[i] != null){
					result = result.add(args[i]);
				}
			}
			result.setScale(Constants.DECIMAL_PLACE_DEFAULT, BigDecimal.ROUND_HALF_UP);
		}
		return result;
	}
	
	/**
	 * 根据金额，费率算出手续费
	 * 
	 * @Description: TODO
	 * @author myz
	 * @param payFee
	 * @param feeRrate
	 * @return
	 */
	public static BigDecimal getTripartiteFee(BigDecimal payFee, String feeRrate) {
		return new BigDecimal(feeRrate).divide(Constants.BIGDECIMAL_ONE_HUNDRED).multiply(payFee)
				.setScale(Constants.DECIMAL_PLACE_DEFAULT, BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * 计算分成金额，四舍五入
	 * 
	 * @Description: TODO
	 * @author myz
	 */
	public static BigDecimal getFeeCeil(BigDecimal payFee, Integer settle) {
		return new BigDecimal(settle).divide(Constants.BIGDECIMAL_ONE_HUNDRED).multiply(payFee).setScale(Constants.DECIMAL_PLACE_DEFAULT, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 计算平台分成
	 * @Description: TODO
	 * @author myz
	 */
	public static BigDecimal getPlatformFeeCeil(BigDecimal actualFee, BigDecimal ownerFee, BigDecimal estateFee) {
		return actualFee.subtract(ownerFee).subtract(estateFee).setScale(Constants.DECIMAL_PLACE_DEFAULT, BigDecimal.ROUND_HALF_UP);
	}

}

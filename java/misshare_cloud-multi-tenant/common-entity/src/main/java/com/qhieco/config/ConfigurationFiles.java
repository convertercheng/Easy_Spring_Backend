package com.qhieco.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author myz
 * @date 2017年12月27日 下午2:45:44
 * 
 */
@Component
@Data
public class ConfigurationFiles {
	@Value("${web.upload-path}")
	private String webUploadPath;

	@Value("${HTTP_PREFIX}")
	private String httpPrefix;

	@Value("${SERVER_IP_DEV}")
	private String serverIpDev;

	@Value("${PORT}")
	private String port;

	@Value("${APP_ID}")
	private String appId;

	@Value("${PRIVATE_KEY}")
	private String privateKey;

	@Value("${ALIPAY_PUBLIC_KEY}")
	private String alipayPublicKey;

	@Value("${WX_OP_APPID}")
	private String wxOpAppId;

	@Value("${OP_MCH_ID}")
	private String opMchId;

	@Value("${SPBILL_CREATE_IP}")
	private String spbillCreateIp;

	@Value("${OP_APICLIENT_CERT}")
	private String opApiclientCert;

	@Value("${PIC_PATH}")
	private String picPath;

	@Value("${ALIPAY_FEE_RATE}")
	private String alipayFeeRate;

	@Value("${WX_FEE_RATE}")
	private String wxFeeRate;

	@Value("${TAX_RATE}")
	private String taxRate;

	@Value("${MP_APICLIENT_CERT}")
	private String mpApiclientCert;

	@Value("${BOOSTED_GOAL_ACCESS_CODE}")
	private String boostedGoalAccessCode;

	@Value("${BOOSTED_GOAL_SECRET_KEY}")
	private String boostedGoalSecretKey;

	@Value("${BOOSTED_GOAL_PARKING_URL}")
	private String boostedGoalParkingUrl;

	@Value("${WX_MP_APPID}")
	private String wxMpAppId;

	@Value("${MP_URL_PREFIX}")
	private String mpUrlPrefix;

	@Value("${MP_MCH_ID}")
	private String mpMchId;

	@Value("${WX_MP_SECRET}")
    private String wxMpSecret;

	@Value("${WX_PUBLIC_FEE_RATE}")
	private String wxPublicFeeRate;

	@Value("${MP_AUTH_URL}")
	private String mpAuthUrl;

	@Value("${KEY_TOP_APP_ID}")
	private String keyTopAppId;

	@Value("${KEY_TOP_SECRET_KEY}")
	private String keyTopSecretKey;

	@Value("${KEY_TOP_PARKING_URL}")
	private String keyTopParkingUrl;

	@Value("${KEY_TOP_CANCEL_URL}")
	private String keyTopCancelUrl;

	@Value("${KEY_TOP_COMPANY_ID}")
	private String keyTopCompanyId;

	@Value("${WX_XCX_APPID}")
	private String wxXcxAppId;

	@Value("${WX_XCX_SECRET}")
	private String WxXcxSecret;

	@Override
	public String toString() {
		return "ConfigurationFiles{" +
				"webUploadPath='" + webUploadPath + '\'' +
				", httpPrefix='" + httpPrefix + '\'' +
				", serverIpDev='" + serverIpDev + '\'' +
				", port='" + port + '\'' +
				", appId='" + appId + '\'' +
				", privateKey='" + privateKey + '\'' +
				", alipayPublicKey='" + alipayPublicKey + '\'' +
				", wxOpAppId='" + wxOpAppId + '\'' +
				", opMchId='" + opMchId + '\'' +
				", spbillCreateIp='" + spbillCreateIp + '\'' +
				", opApiclientCert='" + opApiclientCert + '\'' +
				", picPath='" + picPath + '\'' +
				", alipayFeeRate='" + alipayFeeRate + '\'' +
				", wxFeeRate='" + wxFeeRate + '\'' +
				", taxRate='" + taxRate + '\'' +
				", mpApiclientCert='" + mpApiclientCert + '\'' +
				'}';
	}
}

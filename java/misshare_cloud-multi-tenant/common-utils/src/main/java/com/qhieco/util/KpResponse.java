package com.qhieco.util;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

@XStreamAlias("RESPONSE_FPKJ")
@Data
public class KpResponse {

	private String RETURNCODE;
	private String RETURNMESSAGE;
	private String HJBHSJE;
	private String HJSE;
	private String KPRQ;
	private String SSYF;
	private String FP_DM;
	private String FP_HM;
	private String RETCODE;
	private String FWMW;
	private String JYM;
	private String SZQM;
	private String EWM;
	private String PDF_URL;
	

}

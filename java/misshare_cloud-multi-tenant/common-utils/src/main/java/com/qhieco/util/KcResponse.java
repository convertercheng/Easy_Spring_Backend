package com.qhieco.util;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

@XStreamAlias("RESPONSE_FPKCCX")
@Data
public class KcResponse {

	private String RETURNCODE;
	private String RETURNMESSAGE;
	private String CNT;
	

	
	
}

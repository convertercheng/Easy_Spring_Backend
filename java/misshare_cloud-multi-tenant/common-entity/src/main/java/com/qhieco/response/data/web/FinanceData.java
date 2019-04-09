package com.qhieco.response.data.web;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class FinanceData {

	Income income;
	Outgo outgo;
	BigDecimal platformFee;


	List<Finance> financeList;

	public FinanceData() {
	}

	public FinanceData(List<Finance> financeList) {
		this.financeList = financeList;
	}



	@Data
	public class Income {
		BigDecimal parkingFee;


	}

	@Data
	public class Outgo {
		BigDecimal tripartiteFee;
		BigDecimal ownerFee;
		BigDecimal estateFee;

	}

	@Data
	public class Finance {
		String inflow;
		String outflow;
		String balance;
		Long time;

		public Finance() {
		}

		public Finance(String inflow, String outflow, String balance, Long time) {
			this.inflow = inflow;
			this.outflow = outflow;
			this.balance = balance;
			this.time = time;
		}

	}
}
